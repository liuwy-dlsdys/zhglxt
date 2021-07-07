package com.zhglxt.activiti.service.impl;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.zhglxt.activiti.entity.Act;
import com.zhglxt.activiti.mapper.ActMapper;
import com.zhglxt.common.core.entity.sys.SysUser;
import com.zhglxt.common.util.DateUtils;
import com.zhglxt.common.util.ShiroUtils;
import com.zhglxt.common.util.SpringContextHolder;
import com.zhglxt.common.util.StringUtils;
import com.zhglxt.activiti.util.UserUtils;
import org.activiti.engine.*;
import org.activiti.engine.delegate.Expression;
import org.activiti.engine.history.HistoricActivityInstance;
import org.activiti.engine.history.HistoricProcessInstance;
import org.activiti.engine.repository.ProcessDefinition;
import org.activiti.engine.runtime.ProcessInstance;
import org.activiti.engine.task.Comment;
import org.activiti.engine.task.Task;
import org.activiti.engine.task.TaskQuery;
import org.activiti.spring.ProcessEngineFactoryBean;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import java.util.*;

/**
 * 流程定义相关Service
 * @author liuwy
 * @version 2019/9/25
 */
@Service
@Transactional(readOnly = true)
public class ActTaskService {
    protected final Logger logger = LoggerFactory.getLogger(ActTaskService.class);

    @Autowired
    private ProcessEngineFactoryBean processEngineFactory;
    @Autowired
    private ProcessEngine processEngine;
    @Autowired
    private RuntimeService runtimeService;
    @Autowired
    private TaskService taskService;
    @Autowired
    private FormService formService;
    @Autowired
    private HistoryService historyService;
    @Autowired
    private RepositoryService repositoryService;
    @Autowired
    private IdentityService identityService;

    @Autowired
    private ActMapper actMapper;


    /**
     * 启动流程
     *
     * @param procDefKey    流程定义KEY
     * @param businessTable 业务表表名
     * @param businessId    业务表编号
     * @param title         流程标题，显示在待办任务标题
     * @return 流程实例ID
     */
    @Transactional(readOnly = false)
    public String startProcess(String procDefKey, String businessTable, String businessId, String title) {
        Map<String, Object> vars = Maps.newHashMap();
        return startProcess(procDefKey, businessTable, businessId, title, vars);
    }

    /**
     * 启动流程
     *
     * @param procDefKey    流程定义KEY
     * @param businessTable 业务表表名
     * @param businessId    业务表编号
     * @param title         流程标题，显示在待办任务标题
     * @param vars          流程变量
     * @return 流程实例ID
     */
    @Transactional(readOnly = false)
    public String startProcess(String procDefKey, String businessTable, String businessId, String title, Map<String, Object> vars) {
        //获取当前用户
        SysUser user = ShiroUtils.getSysUser();
        // 用来设置启动流程的人员ID，引擎会自动把用户ID保存到activiti:initiator中
        identityService.setAuthenticatedUserId(user.getLoginName());

        // 设置流程变量
        if (vars == null) {
            vars = Maps.newHashMap();
        }

        // 设置流程标题
        if (StringUtils.isNotBlank(title)) {
            vars.put("title", title);
        }

        // 启动流程
        ProcessInstance procIns = runtimeService.startProcessInstanceByKey(procDefKey, businessTable + ":" + businessId, vars);

        // 更新业务表流程实例ID
        Act act = new Act();
        act.setBusinessTable(businessTable);// 业务表名
        act.setBusinessId(businessId);    // 业务表ID
        act.setProcInsId(procIns.getId());
        actMapper.updateProcInsIdByBusinessId(act);
        return act.getProcInsId();
    }

    /**
     * 提交任务, 并保存意见
     *
     * @param taskId    任务ID
     * @param procInsId 流程实例ID，如果为空，则不保存任务提交意见
     * @param comment   任务提交意见的内容
     * @param title     流程标题，显示在待办任务标题
     * @param vars      任务变量
     */
    @Transactional(readOnly = false)
    public void complete(String taskId, String procInsId, String comment, String title, Map<String, Object> vars) {
        // 添加意见
        if (StringUtils.isNotBlank(procInsId) && StringUtils.isNotBlank(comment)) {
            taskService.addComment(taskId, procInsId, comment);
        }

        // 设置流程变量
        if (vars == null) {
            vars = Maps.newHashMap();
        }

        // 设置流程标题
        if (StringUtils.isNotBlank(title)) {
            vars.put("title", title);
        }

        // 提交任务
        taskService.complete(taskId, vars);
    }

    /**
     * 获取流转历史列表
     *
     * @param paramMap（procInsId 流程实例，startAct 开始活动节点名称，endAct 结束活动节点名称）
     * @return
     */
    public List<Map<String, Object>> histoicFlowList(Map<String, Object> paramMap) {
        List<Map<String, Object>> arrayList = Lists.newArrayList();
        List<HistoricActivityInstance> list = historyService.createHistoricActivityInstanceQuery().processInstanceId(paramMap.get("procInsId").toString())
                .orderByHistoricActivityInstanceStartTime().asc().orderByHistoricActivityInstanceEndTime().asc().list();

        boolean start = false;
        Map<String, Integer> actMap = Maps.newHashMap();

        for (int i = 0; i < list.size(); i++) {

            HistoricActivityInstance histIns = list.get(i);

            // 过滤开始节点前的节点
            if (StringUtils.isNotBlank(paramMap.get("startAct")) && paramMap.get("startAct").equals(histIns.getActivityId())) {
                start = true;
            }
            if (StringUtils.isNotBlank(paramMap.get("startAct")) && !start) {
                continue;
            }

            // 只显示开始节点和结束节点，并且执行人不为空的任务
            if (StringUtils.isNotBlank(histIns.getAssignee())
                    || "startEvent".equals(histIns.getActivityType())
                    || "endEvent".equals(histIns.getActivityType())) {

                // 给节点增加一个序号
                Integer actNum = actMap.get(histIns.getActivityId());
                if (actNum == null) {
                    actMap.put(histIns.getActivityId(), actMap.size());
                }

                Map<String, Object> atMap = Maps.newHashMap();
                atMap.put("histIns", histIns);
                // 获取流程发起人名称
                if ("startEvent".equals(histIns.getActivityType())) {
                    List<HistoricProcessInstance> il = historyService.createHistoricProcessInstanceQuery().processInstanceId(paramMap.get("procInsId").toString()).orderByProcessInstanceStartTime().asc().list();
                    if (il.size() > 0) {
                        if (StringUtils.isNotBlank(il.get(0).getStartUserId())) {
                            SysUser user = UserUtils.getByLoginName(il.get(0).getStartUserId());
                            if (user != null) {
                                atMap.put("assignee", histIns.getAssignee());
                                atMap.put("sssigneeName", user.getUserName());
                            }
                        }
                    }
                }
                // 获取任务执行人名称
                if (StringUtils.isNotEmpty(histIns.getAssignee())) {
                    SysUser user = UserUtils.getByLoginName(histIns.getAssignee());
                    if (user != null) {
                        atMap.put("assignee", histIns.getAssignee());
                        atMap.put("sssigneeName", user.getUserName());
                    }
                }
                // 获取意见评论内容
                if (StringUtils.isNotBlank(histIns.getTaskId())) {
                    List<Comment> commentList = taskService.getTaskComments(histIns.getTaskId());
                    if (!CollectionUtils.isEmpty(commentList)) {
                        atMap.put("comment", commentList.get(0).getFullMessage());
                    }
                }
                //任务历时
                if (StringUtils.isNotBlank(histIns.getEndTime())) {
                    String taskFor = DateUtils.getDatePoor(histIns.getEndTime(), histIns.getStartTime());
                    atMap.put("taskFor", taskFor);
                }
                arrayList.add(atMap);
            }

            // 过滤结束节点后的节点
            if (StringUtils.isNotBlank(paramMap.get("endAct")) && paramMap.get("endAct").equals(histIns.getActivityId())) {
                boolean bl = false;
                Integer actNum = actMap.get(histIns.getActivityId());
                // 该活动节点，后续节点是否在结束节点之前，在后续节点中是否存在
                for (int j = i + 1; j < list.size(); j++) {
                    HistoricActivityInstance hi = list.get(j);
                    Integer actNumA = actMap.get(hi.getActivityId());
                    if ((actNumA != null && actNumA < actNum) || StringUtils.equals(hi.getActivityId(), histIns.getActivityId())) {
                        bl = true;
                    }
                }
                if (!bl) {
                    break;
                }
            }
        }
        return arrayList;
    }

    /**
     * 我的任务 列表
     *
     * @param paramMap
     * @return
     */
    public List<Map<String, Object>> todoList(Map<String, Object> paramMap) {
        SysUser user = ShiroUtils.getSysUser();
        List<Map<String, Object>> resultList = Lists.newArrayList();

        // =============== 已经签收的任务  ===============
        TaskQuery todoTaskQuery = taskService.createTaskQuery().taskAssignee(user.getLoginName()).active()
                .includeProcessVariables().orderByTaskCreateTime().desc();
        // 设置查询条件
        if (paramMap.containsKey("procDefKey")) {
            if(StringUtils.isNotEmpty(paramMap.get("procDefKey").toString())){
                todoTaskQuery.processDefinitionKey(paramMap.get("procDefKey").toString());
            }
        }
        if (paramMap.containsKey("startTime")) {
            if(StringUtils.isNotEmpty(paramMap.get("startTime").toString())){
                todoTaskQuery.taskCreatedAfter(DateUtils.parseDate(paramMap.get("startTime")));
            }
        }
        if (paramMap.containsKey("endTime")) {
            if(StringUtils.isNotEmpty(paramMap.get("endTime").toString())){
                todoTaskQuery.taskCreatedBefore(DateUtils.parseDate(paramMap.get("endTime")));
            }
        }

        // 查询列表
        List<Task> todoList = todoTaskQuery.list();
        for (Task task : todoList) {
            HashMap<String, Object> maps = Maps.newHashMap();

            HashMap<String, Object> taskMap = Maps.newHashMap();
            taskMap.put("id", task.getId());
            taskMap.put("executionId", task.getExecutionId());
            taskMap.put("processInstanceId", task.getProcessInstanceId());
            taskMap.put("processDefinitionId", task.getProcessDefinitionId());
            taskMap.put("taskDefinitionKey", task.getTaskDefinitionKey());
            taskMap.put("name", task.getName());
            taskMap.put("assignee", task.getAssignee());
            taskMap.put("createTime", task.getCreateTime());

            HashMap<String, Object> varsMap = Maps.newHashMap();
            varsMap.put("apply", task.getProcessVariables().get("apply"));
            varsMap.put("title", task.getProcessVariables().get("title"));

            HashMap<String, Object> procDefMap = Maps.newHashMap();
            RepositoryService repositoryService = SpringContextHolder.getBean(RepositoryService.class);
            ProcessDefinition singleResult = repositoryService.createProcessDefinitionQuery().processDefinitionId(task.getProcessDefinitionId()).singleResult();
            procDefMap.put("name", singleResult.getName());
            procDefMap.put("version", singleResult.getVersion());

            maps.put("task", taskMap);
            maps.put("vars", varsMap);
            maps.put("procDef", procDefMap);
            maps.put("status", "todo");

//			map.put("taskVars", task.getTaskLocalVariables());
//			map.put("procIns",runtimeService.createProcessInstanceQuery().processInstanceId(task.getProcessInstanceId()).singleResult());
//			map.put("procExecUrl",ActUtils.getProcExeUrl(task.getProcessDefinitionId()));
//			System.out.println(task.getId()+"  =  "+task.getProcessVariables() + "  ========== " + task.getTaskLocalVariables());
            resultList.add(maps);
        }

        // =============== 等待签收的任务  ===============
        TaskQuery toClaimQuery = taskService.createTaskQuery().taskCandidateUser(user.getLoginName())
                .includeProcessVariables().active().orderByTaskCreateTime().desc();

        // 设置查询条件
        if (paramMap.containsKey("procDefKey")) {
            if(StringUtils.isNotEmpty(paramMap.get("procDefKey").toString())){
                toClaimQuery.processDefinitionKey(paramMap.get("procDefKey").toString());
            }
        }
        if (paramMap.containsKey("startTime")) {
            if(StringUtils.isNotEmpty(paramMap.get("startTime").toString())){
                toClaimQuery.taskCreatedAfter(DateUtils.parseDate(paramMap.get("startTime")));
            }
        }
        if (paramMap.containsKey("endTime")) {
            if(StringUtils.isNotEmpty(paramMap.get("endTime").toString())){
                toClaimQuery.taskCreatedBefore(DateUtils.parseDate(paramMap.get("endTime")));
            }
        }

        // 查询列表
        List<Task> toClaimList = toClaimQuery.list();
        for (Task task : toClaimList) {
            HashMap<String, Object> maps = Maps.newHashMap();
            HashMap<String, Object> taskMap = Maps.newHashMap();
            taskMap.put("id", task.getId());
            taskMap.put("executionId", task.getExecutionId());
            taskMap.put("processInstanceId", task.getProcessInstanceId());
            taskMap.put("processDefinitionId", task.getProcessDefinitionId());
            taskMap.put("taskDefinitionKey", task.getTaskDefinitionKey());
            taskMap.put("name", task.getName());
            taskMap.put("assignee", task.getAssignee());
            taskMap.put("createTime", task.getCreateTime());

            HashMap<String, Object> varsMap = Maps.newHashMap();
            varsMap.put("apply", task.getProcessVariables().get("apply"));
            varsMap.put("title", task.getProcessVariables().get("title"));

            HashMap<String, Object> procDefMap = Maps.newHashMap();
            RepositoryService repositoryService = SpringContextHolder.getBean(RepositoryService.class);
            ProcessDefinition singleResult = repositoryService.createProcessDefinitionQuery().processDefinitionId(task.getProcessDefinitionId()).singleResult();
            procDefMap.put("name", singleResult.getName());
            procDefMap.put("version", singleResult.getVersion());

            maps.put("task", taskMap);
            maps.put("vars", varsMap);
            maps.put("procDef", procDefMap);
            maps.put("status", "claim");


//			map.put("taskVars", task.getTaskLocalVariables());
//			map.put("procIns", runtimeService.createProcessInstanceQuery().processInstanceId(task.getProcessInstanceId()).singleResult());
//			map.put("procExecUrl", ActUtils.getProcExeUrl(task.getProcessDefinitionId()));
//			System.out.println(task.getId()+"  =  "+task.getProcessVariables() + "  ========== " + task.getTaskLocalVariables());
            resultList.add(maps);
        }
        return resultList;
    }

    /**
     * 获取流程表单（首先获取任务节点表单KEY，如果没有则取流程开始节点表单KEY）
     *
     * @param procDefId
     * @param taskDefKey
     * @return
     */
    public String getFormKey(String procDefId, String taskDefKey) {
        String formKey = "";
        if (StringUtils.isNotBlank(procDefId)) {
            if (StringUtils.isNotBlank(taskDefKey)) {
                try {
                    formKey = formService.getTaskFormKey(procDefId, taskDefKey);
                } catch (Exception e) {
                    formKey = "";
                }
            }
            if (StringUtils.isBlank(formKey)) {
                formKey = formService.getStartFormKey(procDefId);
            }
            if (StringUtils.isBlank(formKey)) {
                formKey = "/404";
            }
        }
        logger.debug("getFormKey: {}", formKey);
        return formKey;
    }

    /**
     * 获取流程实例对象
     *
     * @param procInsId
     * @return
     */
    @Transactional(readOnly = false)
    public ProcessInstance getProcIns(String procInsId) {
        return runtimeService.createProcessInstanceQuery().processInstanceId(procInsId).singleResult();
    }

    /**
     * 任务签收
     *
     * @param paramMap
     */
    @Transactional(readOnly = false)
    public boolean claim(Map<String, Object> paramMap) {
        boolean flag = false;
        try {
            SysUser user = ShiroUtils.getSysUser();
            taskService.claim(paramMap.get("taskId").toString(), user.getLoginName());
            flag = true;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return flag;
    }

    /**
     * 删除任务
     *
     * @param paramMap
     */
    @Transactional(readOnly = false)
    public boolean deleteTask(Map<String, Object> paramMap) {
        boolean flag = false;
        try {
            taskService.deleteTask(paramMap.get("taskId").toString(), "删除任务");
            flag = true;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return flag;
    }

    /**
     * 提交任务, 并保存意见
     *
     * @param taskId    任务ID
     * @param procInsId 流程实例ID，如果为空，则不保存任务提交意见
     * @param comment   任务提交意见的内容
     * @param vars      任务变量
     */
    @Transactional(readOnly = false)
    public void complete(String taskId, String procInsId, String comment, Map<String, Object> vars) {
        complete(taskId, procInsId, comment, "", vars);
    }

    /**
     * 设置任务组
     *
     * @param vars
     * @param candidateGroupIdExpressions
     */
    private void setTaskGroup(Map<String, Object> vars, Set<Expression> candidateGroupIdExpressions) {
        String roles = "";
        for (Expression expression : candidateGroupIdExpressions) {
            String expressionText = expression.getExpressionText();
            String roleName = identityService.createGroupQuery().groupId(expressionText).singleResult().getName();
            roles += roleName;
        }
        vars.put("任务所属角色", roles);
    }

    /**
     * 设置当前处理人信息
     *
     * @param vars
     * @param currentTask
     */
    private void setCurrentTaskAssignee(Map<String, Object> vars, Task currentTask) {
        String assignee = currentTask.getAssignee();
        if (assignee != null) {
            org.activiti.engine.identity.User assigneeUser = identityService.createUserQuery().userId(assignee).singleResult();
            String userInfo = assigneeUser.getFirstName() + " " + assigneeUser.getLastName();
            vars.put("当前处理人", userInfo);
        }
    }

}
