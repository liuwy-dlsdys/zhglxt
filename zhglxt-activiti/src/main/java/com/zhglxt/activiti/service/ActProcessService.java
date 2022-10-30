package com.zhglxt.activiti.service;

import com.zhglxt.activiti.entity.ProcessDefinitionDto;
import com.zhglxt.common.core.entity.AjaxResult;
import com.zhglxt.common.core.page.TableDataInfo;
import org.activiti.engine.repository.Model;

import java.io.InputStream;
import java.util.List;
import java.util.Map;

/**
 * 流程管理 服务层接口
 * @author liuwy
 */
public interface ActProcessService {
    /**
     * 将流程定义转换成模型
     *
     * @param processId 流程编号
     * @return Model
     * @throws Exception
     */
    Model convertToModel(String processId) throws Exception;

    /**
     * 使用部署对象ID查看流程图
     *
     * @param deploymentId 部署id
     * @param imageName  图片名称
     * @return InputStream
     * @throws Exception
     */
    InputStream findImageStream(String deploymentId, String imageName) throws Exception;

    /**
     * 查询流程定义
     *
     * @param processDefinition 流程信息
     * @return TableDataInfo
     */
    TableDataInfo selectProcessDefinitionList(ProcessDefinitionDto processDefinition);

    /**
     * 部署流程定义
     *
     * @param is       文件流
     * @param fileName 文件名称
     * @param category 模型分类
     * @return AjaxResult
     */
    AjaxResult saveNameDeplove(InputStream is, String fileName, String category);

    /**
     * 根据流程部署id，删除流程定义
     *
     * @param ids 部署ids
     * @return boolean
     */
    boolean deleteProcessDefinitionByDeploymentIds(String ids);

    /**
     * 查询运行中的流程
     *
     * @param paramMap 参数map
     * @return List<Map<String, Object>>
     */
    List<Map<String, Object>> runningList(Map<String, Object> paramMap);

    /**
     * 根据 流程实例ID 删除流程实例
     *
     * @param paramMap 参数map
     * @return boolean
     */
    boolean deleteProcIns(Map<String, Object> paramMap);

    /**
     * 查询流程List
     *
     * @param paramMap 参数map
     * @return List<Map<String, Object>>
     */
    List<Map<String, Object>> processList(Map<String, Object> paramMap);

    /**
     * 挂起、激活流程实例
     *
     * @param state 状态
     * @param paramMap 参数map
     * @return boolean
     */
    boolean updateState(String state, Map<String, Object> paramMap);

}
