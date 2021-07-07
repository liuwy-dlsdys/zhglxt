package com.zhglxt.web.controller.cms;

import com.google.common.collect.Maps;
import com.zhglxt.cms.entity.HelpDoc;
import com.zhglxt.cms.service.impl.HelpDocServiceImpl;
import com.zhglxt.common.annotation.Log;
import com.zhglxt.common.config.GlobalConfig;
import com.zhglxt.common.core.controller.BaseController;
import com.zhglxt.common.core.entity.AjaxResult;
import com.zhglxt.common.core.page.TableDataInfo;
import com.zhglxt.common.core.text.Convert;
import com.zhglxt.common.enums.BusinessType;
import com.zhglxt.common.util.WebUtil;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Map;

/**
 * @Description 帮助文档 控制层
 * @Author liuwy
 * @Date 2021/6/24
 * @Version 1.0
 **/
@Controller
@RequestMapping("/cms/helpDoc")
public class HelpDocController extends BaseController {
    private String prefix = "cms/helpDoc";

    @Autowired
    private HelpDocServiceImpl helpDocService;

    @RequestMapping
    public String helpDocIndex() {
        return prefix + "/helpDocIndex";
    }

    @RequestMapping("/list")
    @ResponseBody
    public TableDataInfo selectHelpDocList(HttpServletRequest request) {
        Map<String, Object> paramMap = WebUtil.paramsToMap(request.getParameterMap());
        startPage();
        List<HelpDoc> list = helpDocService.selectHelpDocList(paramMap);
        return getDataTable(list);
    }

    @GetMapping("/add")
    public String addHelpDoc() {
        return prefix + "/addHelpDoc";
    }

    /**
     * 新增保存文档
     */
    @Log(title = "帮助文档-新增", businessType = BusinessType.INSERT)
    @RequestMapping("/addSave")
    @ResponseBody
    public AjaxResult addSave(HttpServletRequest request) {
        if (GlobalConfig.isDemoEnabled()) {
            return error("演示模式不允许本操作");
        }
        Map<String, Object> paramMap = WebUtil.paramsToMap(request.getParameterMap());
        return toAjax(helpDocService.addHelpDoc(paramMap));
    }

    /**
     * 跳转到编辑页面
     * @param request
     * @param model
     * @return
     */
    @RequestMapping("/edit")
    public String editHelpDoc(HttpServletRequest request, Model model) {
        Map<String, Object> paramMap = WebUtil.paramsToMap(request.getParameterMap());
        List<HelpDoc> list = helpDocService.selectHelpDocList(paramMap);
        if (!CollectionUtils.isEmpty(list)) {
            model.addAttribute("helpDoc", list.get(0));
        }
        return prefix + "/editHelpDoc";
    }

    /**
     * 修改文章
     */
    @Log(title = "帮助文档-编辑", businessType = BusinessType.UPDATE)
    @PostMapping("/edit")
    @ResponseBody
    public AjaxResult editSave(HttpServletRequest request) {
        if (GlobalConfig.isDemoEnabled()) {
            return error("演示模式不允许本操作");
        }
        Map<String, Object> paramMap = WebUtil.paramsToMap(request.getParameterMap());
        return toAjax(helpDocService.updateHelpDoc(paramMap));
    }

    /**
     * 删除文章
     */
    @Log(title = "帮助文档-删除", businessType = BusinessType.DELETE)
    @RequestMapping("/remove")
    @ResponseBody
    public AjaxResult deleteHelpDoc(String ids) {
        if (GlobalConfig.isDemoEnabled()) {
            return error("演示模式不允许本操作");
        }
        return toAjax(helpDocService.deleteHelpDoc(Convert.toStrArray(ids)));
    }

    @GetMapping("/helpDocView/{id}")
    public String detail(@PathVariable("id") String id, Model model) {
        Map<String, Object> paramMap = Maps.newHashMap();
        paramMap.put("id",id);
        List<HelpDoc> list = helpDocService.selectHelpDocList(paramMap);
        if (!CollectionUtils.isEmpty(list)) {
            model.addAttribute("helpDoc", list.get(0));
        }
        return prefix + "/helpDocView";
    }
}
