package com.zhglxt.activiti.service;

import com.fasterxml.jackson.databind.node.ObjectNode;
import com.zhglxt.activiti.entity.ModelEntityDto;
import com.zhglxt.common.core.entity.AjaxResult;
import com.zhglxt.common.core.page.TableDataInfo;
import org.activiti.engine.repository.Model;

import java.util.List;
import java.util.Map;
/**
 * 模型管理 服务层接口
 * @author liuwy
 */
public interface ActModelService {
    /**
     * 查询模型列表
     *
     * @param modelEntityDto 模型信息
     * @return 模型集合
     */
    TableDataInfo selectModelList(ModelEntityDto modelEntityDto);

    /**
     * 查询模型编辑器
     *
     * @param modelId 模型ID
     * @return json信息
     */
    ObjectNode selectWrapModelById(String modelId);

    /**
     * 查询模型信息
     *
     * @param modelId 模型ID
     * @return 模型信息
     */
    Model selectModelById(String modelId);

    /**
     * 修改模型信息
     *
     * @param model 模型信息
     * @param name 名称
     * @param description 描述信息
     * @param json_xml json参数
     * @param svg_xml  xml参数
     */
    void update(Model model, String name, String description, String json_xml, String svg_xml);

    /**
     * 批量删除模型信息
     *
     * @param ids 需要删除的ID列表
     * @return boolean
     */
    boolean deleteModelIds(String ids);

    /**
     * 获取资源文件信息
     *
     * @param modelId 模型ID
     * @return byte[]
     */
    byte[] getModelEditorSource(String modelId);

    /**
     * 模型列表
     *
     * @param paramMap 参数map
     * @return List<Map<String, Object>>
     */
    List<Map<String, Object>> modelList(Map<String, Object> paramMap);

    /**
     * 部署流程
     *
     * @param paramMap 参数map
     * @return AjaxResult
     */
    AjaxResult deployModel(Map<String, Object> paramMap);

    /**
     * 添加模型
     *
     * @param paramMap 参数map
     */
    void createModle(Map<String, Object> paramMap);

}
