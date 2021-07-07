package com.zhglxt.web.controller.oa;


import com.zhglxt.common.annotation.Log;
import com.zhglxt.common.config.GlobalConfig;
import com.zhglxt.common.core.controller.BaseController;
import com.zhglxt.common.core.entity.AjaxResult;
import com.zhglxt.common.core.entity.sys.SysUser;
import com.zhglxt.common.core.page.TableDataInfo;
import com.zhglxt.common.enums.BusinessType;
import com.zhglxt.common.util.ShiroUtils;
import com.zhglxt.common.util.StringUtils;
import com.zhglxt.common.util.WebUtil;
import com.zhglxt.oa.entity.Leave;
import com.zhglxt.oa.service.impl.LeaveServiceImpl;
import com.zhglxt.system.service.ISysPostService;
import com.zhglxt.system.service.ISysRoleService;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Map;

/**
 * 请假申请Controller
 * @author liuwy
 * @version 2019/9/27
 */
@Controller
@RequestMapping("/oa/leave")
public class LeaveController extends BaseController {

    private String prefix = "oa/";

    @Autowired
    private LeaveServiceImpl leaveService;

    @Autowired
    private ISysRoleService roleService;

    @Autowired
    private ISysPostService postService;

    @RequiresPermissions("activiti:leave:list")
    @RequestMapping("/list")
    public String leaveList() {
        return prefix + "leaveList";
    }

    @RequiresPermissions("activiti:leave:list")
    @RequestMapping("/getLeaveList")
    @ResponseBody
    public TableDataInfo getLeaveList(HttpServletRequest request) {
        startPage();
        Map<String, Object> paramMap = WebUtil.paramsToMap(request.getParameterMap());
        List<Leave> list = leaveService.getLeaveList(paramMap);
        return getDataTable(list);
    }

    @RequiresPermissions("activiti:leave:add")
    @RequestMapping("/add")
    public String add(ModelMap mmap) {
        SysUser user = ShiroUtils.getSysUser();
//		mmap.put("roles", roleService.selectRoleAll());
        mmap.put("posts", postService.selectPostAll());
        mmap.put("user", user);
        return prefix + "leaveAdd";
    }

    @Log(title = "请假申请管理", businessType = BusinessType.INSERT)
    @RequestMapping("/save")
    @ResponseBody
    public AjaxResult save(HttpServletRequest request) {
        if (GlobalConfig.isDemoEnabled()) {
            return error("演示模式不允许本操作");
        }
        Map<String, Object> paramMap = WebUtil.paramsToMap(request.getParameterMap());
        String flag = leaveService.save(paramMap);
        if (flag.equals("1")) {
            return AjaxResult.success(StringUtils.format("保存成功"));
        } else if (flag.equals("2")) {
            return AjaxResult.success(StringUtils.format("操作成功"));
        } else {
            return AjaxResult.error(StringUtils.format("保存失败"));
        }

    }

    /**
     * 根据 ID 删除请假信息
     *
     * @param request
     * @return
     */
    @Log(title = "删除请假信息", businessType = BusinessType.UPDATE)
    @RequiresPermissions("activiti:leave:remove")
    @RequestMapping("/remove")
    @ResponseBody
    public AjaxResult updateLeaveInFoByIds(HttpServletRequest request) {
        Map<String, Object> paramMap = WebUtil.paramsToMap(request.getParameterMap());
        if (GlobalConfig.isDemoEnabled()) {
            return error("演示模式不允许本操作");
        }
        return toAjax(leaveService.updateLeaveInFoToArray(paramMap));
    }

    /**
     * 表单跳转
     *
     * @return
     */
    @RequestMapping("/form")
    public String form(Leave leave, Model model) {
        String view = "leaveAuditForm";
        Leave lv = new Leave();
        if (StringUtils.isNotBlank(leave.getId())) {

            // 环节编号
            String taskDefKey = leave.getAct().getTaskDefKey();

            lv = leaveService.getLeaveById(leave);

            //请假详情
            if (leave.getAct().isFinishTask()) {
                view = "leaveAuditView";
            }// 修改环节
            else if ("modifyApply".equals(taskDefKey)) {
                lv.setAct(leave.getAct());
                view = "leaveAuditForm";
            }// 部门经理审核环节
            else if ("deptAudit".equals(taskDefKey)) {
                lv.setAct(leave.getAct());
                view = "leaveAudit";
            }
            // HR审核环节
            else if ("HRAudit".equals(taskDefKey)) {
                lv.setAct(leave.getAct());
                view = "leaveAudit";
            }
            // 总经理审核环节
            else if ("ceoAudit".equals(taskDefKey)) {
                lv.setAct(leave.getAct());
                view = "leaveAudit";
            }
            // 销假审核环节
            else if ("xiaojia".equals(taskDefKey)) {
                lv.setAct(leave.getAct());
                view = "leaveAudit";
            }
        }
        model.addAttribute("leave", lv);
        return prefix + view;
    }

    /**
     * 工单执行（完成任务）
     *
     * @param leave
     * @return
     */
    @RequestMapping("/saveLeaveAudit")
    @ResponseBody
    public AjaxResult saveAudit(Leave leave) {
        if (GlobalConfig.isDemoEnabled()) {
            return error("演示模式不允许本操作");
        }
        if (!"xiaojia".equals(leave.getAct().getTaskDefKey())) {
            if (StringUtils.isBlank(leave.getAct().getFlag()) || StringUtils.isBlank(leave.getAct().getComment())) {
                return AjaxResult.error(StringUtils.format("请检查任务流转标识是否正确或者审批意见是否为空。"));
            }
        }
        return AjaxResult.success(leaveService.auditSave(leave));
    }

}
