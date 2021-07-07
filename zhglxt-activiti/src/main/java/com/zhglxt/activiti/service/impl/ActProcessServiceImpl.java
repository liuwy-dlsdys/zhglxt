package com.zhglxt.activiti.service.impl;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.google.common.collect.Maps;
import com.zhglxt.activiti.entity.ProcessDefinitionDto;
import com.zhglxt.activiti.service.ActProcessService;
import com.zhglxt.common.core.entity.AjaxResult;
import com.zhglxt.common.core.page.TableDataInfo;
import com.zhglxt.common.core.text.Convert;
import com.zhglxt.common.util.StringUtils;
import org.activiti.bpmn.converter.BpmnXMLConverter;
import org.activiti.bpmn.model.BpmnModel;
import org.activiti.editor.constants.ModelDataJsonConstants;
import org.activiti.editor.language.json.converter.BpmnJsonConverter;
import org.activiti.engine.RepositoryService;
import org.activiti.engine.RuntimeService;
import org.activiti.engine.repository.Deployment;
import org.activiti.engine.repository.Model;
import org.activiti.engine.repository.ProcessDefinition;
import org.activiti.engine.repository.ProcessDefinitionQuery;
import org.activiti.engine.runtime.ProcessInstance;
import org.activiti.engine.runtime.ProcessInstanceQuery;
import org.apache.commons.io.FilenameUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLStreamReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.zip.ZipInputStream;

/**
 * 流程管理 服务层实现层
 * @author liuwy
 */
@Service
public class ActProcessServiceImpl implements ActProcessService {
    @Autowired
    RepositoryService repositoryService;

    @Autowired
    RuntimeService runtimeService;

    @Autowired
    protected ObjectMapper objectMapper;

    /**
     * 将流程定义转换成模型
     *
     * @param processId 流程编号
     * @return 模型数据
     * @throws Exception
     */
    @Override
    public Model convertToModel(String processId) throws Exception {
        ProcessDefinition processDefinition = repositoryService.createProcessDefinitionQuery()
                .processDefinitionId(processId).singleResult();
        InputStream bpmnStream = repositoryService.getResourceAsStream(processDefinition.getDeploymentId(),
                processDefinition.getResourceName());
        XMLInputFactory xif = XMLInputFactory.newInstance();
        InputStreamReader in = new InputStreamReader(bpmnStream, "UTF-8");
        XMLStreamReader xtr = xif.createXMLStreamReader(in);
        BpmnModel bpmnModel = new BpmnXMLConverter().convertToBpmnModel(xtr);

        BpmnJsonConverter converter = new BpmnJsonConverter();
        ObjectNode modelNode = converter.convertToJson(bpmnModel);
        Model modelData = repositoryService.newModel();
        modelData.setKey(processDefinition.getKey());
        modelData.setName(processDefinition.getResourceName());
        modelData.setCategory(processDefinition.getCategory());
        modelData.setDeploymentId(processDefinition.getDeploymentId());
        modelData.setVersion(Integer.parseInt(
                String.valueOf(repositoryService.createModelQuery().modelKey(modelData.getKey()).count() + 1)));

        ObjectNode modelObjectNode = new ObjectMapper().createObjectNode();
        modelObjectNode.put(ModelDataJsonConstants.MODEL_NAME, processDefinition.getName());
        modelObjectNode.put(ModelDataJsonConstants.MODEL_REVISION, modelData.getVersion());
        modelObjectNode.put(ModelDataJsonConstants.MODEL_DESCRIPTION, processDefinition.getDescription());
        modelData.setMetaInfo(modelObjectNode.toString());

        repositoryService.saveModel(modelData);
        repositoryService.addModelEditorSource(modelData.getId(), modelNode.toString().getBytes("utf-8"));
        return modelData;
    }

    /**
     * 使用部署对象ID查看流程图
     *
     * @param deploymentId 部署id
     * @param imageName    资源文件名
     * @return 文件流
     */
    @Override
    public InputStream findImageStream(String deploymentId, String imageName) throws Exception {
        return repositoryService.getResourceAsStream(deploymentId, imageName);
    }

    /**
     * 查询流程定义
     *
     * @param processDefinition 流程信息
     * @return 流程集合
     */
    @Override
    public TableDataInfo selectProcessDefinitionList(ProcessDefinitionDto processDefinition) {
        TableDataInfo data = new TableDataInfo();
        ProcessDefinitionQuery pdQuery = repositoryService.createProcessDefinitionQuery();
        if (StringUtils.isNotEmpty(processDefinition.getKey())) {
            pdQuery.processDefinitionKey(processDefinition.getKey());
        }
        if (StringUtils.isNotEmpty(processDefinition.getName())) {
            pdQuery.processDefinitionName(processDefinition.getName());
        }
        if (StringUtils.isNotEmpty(processDefinition.getDeploymentId())) {
            pdQuery.deploymentId(processDefinition.getDeploymentId());
        }
        data.setTotal(pdQuery.count());
        data.setRows(pdQuery.orderByDeploymentId().desc()
                .listPage(processDefinition.getPageNum(), processDefinition.getPageSize()).stream()
                .map(ProcessDefinitionDto::new).collect(Collectors.toList()));
        return data;
    }

    /**
     * 部署流程定义
     *
     * @param is       文件流
     * @param fileName 文件名称
     * @param category 类型
     * @return 结果
     */
    @Override
    public AjaxResult saveNameDeplove(InputStream is, String fileName, String category) {
        ZipInputStream zipInputStream = null;
        try {
            String extension = FilenameUtils.getExtension(fileName);
            if (extension.equals("zip")) {
                zipInputStream = new ZipInputStream(is);
                // 创建流程定义
                Deployment deployment = repositoryService.createDeployment().name(fileName)
                        .addZipInputStream(zipInputStream).deploy();
                ProcessDefinition processDefinition = repositoryService.createProcessDefinitionQuery()
                        .deploymentId(deployment.getId()).singleResult();
                repositoryService.setProcessDefinitionCategory(processDefinition.getId(), category);
                return AjaxResult.success(StringUtils.format("部署成功，流程编号[{}]", processDefinition.getId()));
            } else {
                throw new Exception("不支持的文件类型：" + extension);
            }
        } catch (Exception e) {
            return AjaxResult.error(e.getMessage());
        }
    }

    /**
     * 根据流程部署id，删除流程定义
     *
     * @param ids 部署ids
     * @return 结果
     */
    @Override
    public boolean deleteProcessDefinitionByDeploymentIds(String ids) {
        boolean result = true;
        try {
            String[] deploymentIds = Convert.toStrArray(ids);
            for (String deploymentId : deploymentIds) {
                // 级联删除，不管流程是否启动，都能可以删除
                repositoryService.deleteDeployment(deploymentId, true);
            }
        } catch (Exception e) {
            result = false;
            throw e;
        }
        return result;
    }
    //===========================================================================================

    /**
     * 流程管理列表
     */
    @Override
    public List<Map<String, Object>> processList(Map<String, Object> paramMap) {
        ProcessDefinitionQuery processDefinitionQuery = repositoryService.createProcessDefinitionQuery().orderByProcessDefinitionVersion().desc();
//		    		.latestVersion().orderByProcessDefinitionKey().asc();
        if (StringUtils.isNotEmpty(paramMap.get("category").toString())) {
            processDefinitionQuery.processDefinitionCategory(paramMap.get("category").toString());
        }
        if (StringUtils.isNotEmpty(paramMap.get("name").toString())) {
            processDefinitionQuery.processDefinitionName(paramMap.get("name").toString());
        }
        if (StringUtils.isNotEmpty(paramMap.get("deploymentId").toString())) {
            processDefinitionQuery.deploymentId(paramMap.get("deploymentId").toString());
        }

        int firstResult = (Integer.parseInt(paramMap.get("pageNum").toString()) - 1) * Integer.parseInt(paramMap.get("pageSize").toString());
        if (firstResult >= processDefinitionQuery.count()) {
            firstResult = 0;
        }
        int maxResults = Integer.parseInt(paramMap.get("pageSize").toString());

        List<ProcessDefinition> processDefinitionList = processDefinitionQuery.listPage(firstResult, maxResults);

        List<Map<String, Object>> processListMap = new ArrayList<>();
        for (ProcessDefinition processDefinition : processDefinitionList) {
            Map<String, Object> processMap = new HashMap<>();
            processMap.put("id", processDefinition.getId());
            processMap.put("key", processDefinition.getKey());
            processMap.put("name", processDefinition.getName());
            processMap.put("resourceName", processDefinition.getResourceName());
            processMap.put("tenantId", processDefinition.getTenantId());
            processMap.put("version", processDefinition.getVersion());
            processMap.put("category", processDefinition.getCategory());
            processMap.put("deploymentId", processDefinition.getDeploymentId());
            processMap.put("description", processDefinition.getDescription());
            processMap.put("diagramResourceName", processDefinition.getDiagramResourceName());
            processMap.put("suspended", processDefinition.isSuspended());


            String deploymentId = processDefinition.getDeploymentId();
            Deployment deployment = repositoryService.createDeploymentQuery().deploymentId(deploymentId).singleResult();

            processMap.put("deploymentTime", deployment.getDeploymentTime());
            processListMap.add(processMap);
        }
        return processListMap;
    }

    /**
     * 查看运行中的流程
     */
    @Override
    public List<Map<String, Object>> runningList(Map<String, Object> paramMap) {
        List<Map<String, Object>> listMap = new ArrayList<>();
        ProcessInstanceQuery processInstanceQuery = runtimeService.createProcessInstanceQuery();
        if (StringUtils.isNotBlank(paramMap.get("processId"))) {
            processInstanceQuery.processInstanceId(paramMap.get("processId").toString());
        }
        if (StringUtils.isNotBlank(paramMap.get("procDefKey"))) {
            processInstanceQuery.processDefinitionKey(paramMap.get("procDefKey").toString());
        }
        int firstResult = (Integer.parseInt(paramMap.get("pageNum").toString()) - 1) * Integer.parseInt(paramMap.get("pageSize").toString());
        if (firstResult >= processInstanceQuery.count()) {
            firstResult = 0;
        }
        int maxResults = Integer.parseInt(paramMap.get("pageSize").toString());
        List<ProcessInstance> list = processInstanceQuery.listPage(firstResult, maxResults);
        // 循环遍历
        for (ProcessInstance pi : list) {
            HashMap<String, Object> piMap = Maps.newHashMap();
            piMap.put("id", pi.getId());
            piMap.put("name", pi.getName());
            piMap.put("parentId", pi.getParentId());
            piMap.put("processDefinitionId", pi.getProcessDefinitionId());
            piMap.put("processDefinitionKey", pi.getProcessDefinitionKey());
            piMap.put("processDefinitionName", pi.getProcessDefinitionName());
            piMap.put("processInstanceId", pi.getProcessInstanceId());
            piMap.put("tenantId", pi.getTenantId());
            piMap.put("processDefinitionVersion", pi.getProcessDefinitionVersion());
            piMap.put("processVariables", pi.getProcessVariables());
            piMap.put("suspended", pi.isSuspended());
            piMap.put("activityId", pi.getActivityId());
            piMap.put("businessKey", pi.getBusinessKey());
            piMap.put("deploymentId", pi.getDeploymentId());
            piMap.put("description", pi.getDescription());
            piMap.put("localizedDescription", pi.getLocalizedDescription());
            piMap.put("localizedName", pi.getLocalizedName());
            listMap.add(piMap);
        }
        return listMap;
    }

    /**
     * 根据 流程实例ID 删除流程实例
     */
    @Transactional(readOnly = false)
    @Override
    public boolean deleteProcIns(Map<String, Object> paramMap) {
        boolean result = true;
        try {
            runtimeService.deleteProcessInstance(paramMap.get("ids").toString(), "");
        } catch (Exception e) {
            result = false;
            throw e;
        }
        return result;
    }

    /**
     * 挂起、激活流程实例
     */
    @Transactional(readOnly = false)
    @Override
    public boolean updateState(String state, Map<String, Object> paramMap) {
        boolean flag = false;
        if (state.equals("active")) {
            repositoryService.activateProcessDefinitionById(paramMap.get("procDefId").toString(), true, null);
            flag = true;
        } else if (state.equals("suspend")) {
            repositoryService.suspendProcessDefinitionById(paramMap.get("procDefId").toString(), true, null);
            flag = true;
        }
        return flag;
    }

}
