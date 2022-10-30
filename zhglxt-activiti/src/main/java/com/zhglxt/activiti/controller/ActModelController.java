package com.zhglxt.activiti.controller;


import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.zhglxt.activiti.entity.ModelEntityDto;
import com.zhglxt.activiti.service.ActModelService;
import com.zhglxt.common.annotation.Log;
import com.zhglxt.common.config.GlobalConfig;
import com.zhglxt.common.core.controller.BaseController;
import com.zhglxt.common.core.entity.AjaxResult;
import com.zhglxt.common.core.page.TableDataInfo;
import com.zhglxt.common.enums.BusinessType;
import com.zhglxt.common.util.WebUtil;
import org.activiti.bpmn.converter.BpmnXMLConverter;
import org.activiti.bpmn.model.BpmnModel;
import org.activiti.editor.language.json.converter.BpmnJsonConverter;
import org.activiti.engine.ActivitiException;
import org.activiti.engine.repository.Model;
import org.apache.commons.io.IOUtils;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.util.List;
import java.util.Map;

/**
 * 模型管理 操作处理
 * @author liuwy
 */
@Controller
@RequestMapping("/activiti/model")
public class ActModelController extends BaseController {
    private String prefix = "activiti/model";

    @Autowired
    private ActModelService actModelService;

    @RequiresPermissions("activiti:model:view")
    @RequestMapping("/index")
    public String index() {
        return prefix + "/modelList";
    }

    @RequiresPermissions("activiti:model:list")
    @PostMapping("/list")
    @ResponseBody
    public TableDataInfo list(ModelEntityDto modelEntityDto) {
        return actModelService.selectModelList(modelEntityDto);
    }

    /**
     * 跳转到-新增模型
     */
    @GetMapping("/add")
    public String addModel() {
        return prefix + "/addModel";
    }

    /**
     * 创建模型
     */
    @RequiresPermissions("activiti:model:add")
    @Log(title = "创建模型", businessType = BusinessType.INSERT)
    @RequestMapping("/createModle")
    @ResponseBody
    public AjaxResult createModle(HttpServletRequest request) {
        if (GlobalConfig.isDemoEnabled()) {
            return error("演示模式不允许本操作");
        }
        Map<String, Object> paramMap = WebUtil.paramsToMap(request.getParameterMap());
        actModelService.createModle(paramMap);
        return AjaxResult.success();
    }

    @GetMapping(value = "/editor/stencilset", produces = "application/json;charset=utf-8")
    @ResponseBody
    public String getStencilset() {
        InputStream stencilsetStream = this.getClass().getClassLoader().getResourceAsStream("stencilset.json");
        try {
            return IOUtils.toString(stencilsetStream, "utf-8");
        } catch (Exception e) {
            throw new ActivitiException("导入 stencil 文件出现错误", e);
        }
    }

    @GetMapping("/edit/{modelId}")
    public String edit(@PathVariable("modelId") String modelId) {
        return redirect("/modeler.html?modelId=" + modelId);
    }

    @Log(title = "模型管理-保存", businessType = BusinessType.UPDATE)
    @PutMapping(value = "/{modelId}/save")
    @ResponseBody
    public AjaxResult save(@PathVariable String modelId, String name, String description, String json_xml, String svg_xml){
        if (GlobalConfig.isDemoEnabled()) {
            return error("演示模式不允许本操作");
        }
        Model model = actModelService.selectModelById(modelId);
        actModelService.update(model,name,description,json_xml, svg_xml);
        return AjaxResult.success();
    }

    @GetMapping("/{modelId}/json")
    @ResponseBody
    public ObjectNode getEditorJson(@PathVariable String modelId) {
        ObjectNode modelNode = actModelService.selectWrapModelById(modelId);
        return modelNode;
    }

    @Log(title = "删除模型", businessType = BusinessType.DELETE)
    @RequiresPermissions("activiti:model:remove")
    @PostMapping("/remove")
    @ResponseBody
    public AjaxResult remove(String ids) {
        if (GlobalConfig.isDemoEnabled()) {
            return error("演示模式不允许本操作");
        }
        return toAjax(actModelService.deleteModelIds(ids));
    }

    @Log(title = "导出指定模型", businessType = BusinessType.EXPORT)
    @RequiresPermissions("activiti:model:export")
    @RequestMapping("/export/{id}")
    public void exportToXml(@PathVariable("id") String id, HttpServletResponse response) {
        try {
            Model modelData = actModelService.selectModelById(id);
            BpmnJsonConverter jsonConverter = new BpmnJsonConverter();
            JsonNode editorNode = new ObjectMapper().readTree(actModelService.getModelEditorSource(modelData.getId()));
            BpmnModel bpmnModel = jsonConverter.convertToBpmnModel(editorNode);
            BpmnXMLConverter xmlConverter = new BpmnXMLConverter();
            byte[] bpmnBytes = xmlConverter.convertToXML(bpmnModel);

            ByteArrayInputStream in = new ByteArrayInputStream(bpmnBytes);
            IOUtils.copy(in, response.getOutputStream());
            String filename = bpmnModel.getMainProcess().getId() + ".bpmn20.xml";
            response.setHeader("Content-Disposition", "attachment; filename=" + filename);
            response.flushBuffer();
        } catch (Exception e) {
            throw new ActivitiException("导出model的xml文件失败，模型ID=" + id, e);
        }
    }

    /**
     * 模型管理列表
     */
    @RequestMapping("/modelList")
    @ResponseBody
    public TableDataInfo modelList(HttpServletRequest request) {
        Map<String, Object> paramMap = WebUtil.paramsToMap(request.getParameterMap());
        paramMap.put("category", "");
        startPage();
        List<Map<String, Object>> list = actModelService.modelList(paramMap);
        return getDataTable(list);
    }

    @RequestMapping("/editModel")
    public String editModel(HttpServletRequest request) {
        Map<String, Object> paramMap = WebUtil.paramsToMap(request.getParameterMap());
        return redirect("/modeler.html?modelId=" + paramMap.get("modelId").toString());
    }

    @Log(title = "部署模型", businessType = BusinessType.UPDATE)
    @RequiresPermissions("activiti:model:deploy")
    @RequestMapping("/deployModel")
    @ResponseBody
    public AjaxResult deployModel(HttpServletRequest request) {
        if (GlobalConfig.isDemoEnabled()) {
            return error("演示模式不允许本操作");
        }
        Map<String, Object> paramMap = WebUtil.paramsToMap(request.getParameterMap());
        return actModelService.deployModel(paramMap);
    }
}
