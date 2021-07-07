package com.zhglxt.oa.service.impl;

import com.google.common.collect.Maps;
import com.zhglxt.activiti.service.impl.ActTaskService;
import com.zhglxt.activiti.util.ActUtils;
import com.zhglxt.common.core.entity.sys.SysUser;
import com.zhglxt.common.core.text.Convert;
import com.zhglxt.common.util.DateUtils;
import com.zhglxt.common.util.ShiroUtils;
import com.zhglxt.common.util.uuid.IdUtils;
import com.zhglxt.oa.entity.Leave;
import com.zhglxt.oa.mapper.LeaveMapper;
import com.zhglxt.oa.service.ILeaveService;
import com.zhglxt.system.service.ISysRoleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

/**
 * @Description 请假业务实现层
 * @Author liuwy
 * @Date 2019/9/27
 **/
@Service
public class LeaveServiceImpl implements ILeaveService {

    @Autowired
    private LeaveMapper leaveMapper;

    @Autowired
    private ActTaskService actTaskService;

    @Autowired
    private ISysRoleService roleService;

    @Override
    public List<Leave> getLeaveList(Map<String, Object> paramMap) {
        SysUser user = ShiroUtils.getSysUser();
        // 获取当前登录人所有角色列表
        Set<String> roles = new HashSet<String>();
        roles = roleService.selectRoleKeys(user.getUserId());
        if (roles.contains("common")) {//普通用户
            paramMap.put("userId", user.getUserId());
        }
        if (roles.contains("deptAdmin")) {//部门经理
            paramMap.put("userId", null);
            paramMap.put("deptId", user.getDeptId());
        }
        return leaveMapper.getLeaveList(paramMap);
    }

    @Transactional(readOnly = false)
    public String save(Map<String, Object> paramMap) {
        String resoult = "0";
        //获取当前用户
        SysUser user = ShiroUtils.getSysUser();
        if (!paramMap.containsKey("id")) {
            //新增保存

            paramMap.put("id", IdUtils.fastSimpleUUID());
            paramMap.put("createBy", user.getUserId());
            paramMap.put("createDate", new Date());
            paramMap.put("updateBy", user.getUserId());
            paramMap.put("updateDate", new Date());
            paramMap.put("delFlag", 0);
            //插入数据库
            leaveMapper.insert(paramMap);

            // 启动流程
            actTaskService.startProcess(ActUtils.PD_LEAVE[0], ActUtils.PD_LEAVE[1], paramMap.get("id").toString(), paramMap.get("content").toString());
            resoult = "1";
        } else {
            //更新操作
            paramMap.put("updateBy", user.getUserId());
            paramMap.put("updateDate", new Date());
            leaveMapper.update(paramMap);
            paramMap.put("act.comment", "yes".equals(paramMap.get("act.flag").toString()) ? "[重申] " : "[销毁] ");
            // 完成流程任务
            Map<String, Object> vars = Maps.newHashMap();
            vars.put("flag", "yes".equals(paramMap.get("act.flag").toString()) ? "1" : "0");
            actTaskService.complete(paramMap.get("act.taskId").toString(), paramMap.get("act.procInsId").toString(), paramMap.get("act.comment").toString(), paramMap.get("content").toString(), vars);
            resoult = "2";
        }
        return resoult;
    }

    /**
     * 根据id 删除请假信息
     */
    @Override
    @Transactional
    public int updateLeaveInFoByIds(Map paramsToMap) {
        List<String> idList = Arrays.asList(paramsToMap.get("ids").toString().split(","));
        paramsToMap.put("ids",idList);
        return leaveMapper.updateLeaveInFoByIds(paramsToMap);
    }

    /**
     * 根据id 删除请假信息
     */
    @Override
    @Transactional
    public int updateLeaveInFoToList(Map paramsToMap) {
        String[] ids = paramsToMap.get("ids").toString().split(",");
        List<String> idList = Arrays.asList(ids);
        return leaveMapper.updateLeaveInFoToList(idList);
    }

    /**
     * 根据id 删除请假信息
     */
    @Override
    @Transactional
    public int updateLeaveInFoToArray(Map paramsToMap) {
        String[] idArray = Convert.toStrArray(paramsToMap.get("ids").toString());
        return leaveMapper.updateLeaveInFoToArray(idArray);
    }

    /**
     * 根据id查询请假信息
     *
     * @return
     */
    public Leave getLeaveById(Leave leave) {
        return leaveMapper.getLeaveById(leave);
    }

    /**
     * 审核审批保存
     *
     * @param leave
     * @return
     */
    @Transactional(readOnly = false)
    public int auditSave(Leave leave) {
        int i = 1;
        // 设置意见
        leave.getAct().setComment(("yes".equals(leave.getAct().getFlag()) ? "[同意] " : "[驳回] ") + leave.getAct().getComment());

        leave.preUpdate();

        // 对不同环节的业务逻辑进行操作
        String taskDefKey = leave.getAct().getTaskDefKey();

        // 提交流程变量
        Map<String, Object> vars = Maps.newHashMap();

        // 审核环节
        if ("audit".equals(taskDefKey)) {

        }
        //更新部门经理审批意见
        else if ("deptAudit".equals(taskDefKey)) {
            leave.setDeptText(leave.getAct().getComment());
            leaveMapper.updateDeptText(leave);
        }
        //更新HR审批意见
        else if ("HRAudit".equals(taskDefKey)) {
            leave.setHrText(leave.getAct().getComment());
            leaveMapper.updateHrText(leave);
            //计算请假是否大于3天
            Map<String, Object> datePoor = DateUtils.getDatePoorMap(leave.getStartTime(), leave.getEndTime());
            vars.put("day", datePoor.get("day"));
        }
        //更新总经理审批意见
        else if ("ceoAudit".equals(taskDefKey)) {
            leave.setZjlText(leave.getAct().getComment());
            leaveMapper.updateZjlText(leave);
        } else if ("xiaojia".equals(taskDefKey)) {
            leave.getAct().setComment("[同意]");
        }
        // 未知环节，直接返回
        else {
            return i;
        }

        vars.put("flag", "yes".equals(leave.getAct().getFlag()) ? "1" : "0");
        actTaskService.complete(leave.getAct().getTaskId(), leave.getAct().getProcInsId(), leave.getAct().getComment(), vars);
        i = 0;
        return i;
    }

}
