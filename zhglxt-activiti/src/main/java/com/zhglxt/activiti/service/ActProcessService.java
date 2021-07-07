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
     * @return 模型数据
     * @throws Exception
     */
    public Model convertToModel(String processId) throws Exception;

    /**
     * 使用部署对象ID查看流程图
     *
     * @param deploymentId 部署id
     * @param imageName    资源文件名
     * @return 文件流
     */
    public InputStream findImageStream(String deploymentId, String imageName) throws Exception;

    /**
     * 查询流程定义
     *
     * @param processDefinition 流程信息
     * @return 流程集合
     */
    public TableDataInfo selectProcessDefinitionList(ProcessDefinitionDto processDefinition);

    /**
     * 部署流程定义
     *
     * @param is       文件流
     * @param fileName 文件名称
     * @param category 类型
     * @return 结果
     */
    public AjaxResult saveNameDeplove(InputStream is, String fileName, String category);

    /**
     * 根据流程部署id，删除流程定义
     *
     * @param ids 部署ids
     * @return 结果
     */
    public boolean deleteProcessDefinitionByDeploymentIds(String ids);

    //==============================================================================================================

    /**
     * 查询运行中的流程
     *
     * @param paramMap
     * @return
     */
    public List<Map<String, Object>> runningList(Map<String, Object> paramMap);

    /**
     * 根据 流程实例ID 删除流程实例
     *
     * @param paramMap
     * @return
     */
    public boolean deleteProcIns(Map<String, Object> paramMap);

    /**
     * 查询流程List
     *
     * @param paramMap
     * @return
     */
    public List<Map<String, Object>> processList(Map<String, Object> paramMap);

    /**
     * 挂起、激活流程实例
     *
     * @param state
     * @param paramMap
     * @return
     */
    public boolean updateState(String state, Map<String, Object> paramMap);

}
