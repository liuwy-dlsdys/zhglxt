package com.zhglxt.oa.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.zhglxt.activiti.persistence.ActEntity;
import com.zhglxt.common.core.entity.sys.SysDept;
import com.zhglxt.common.core.entity.sys.SysUser;

import java.util.Date;

/**
 * @Description 请假实体
 * @Author liuwy
 * @Date 2019/9/27
 **/
public class Leave extends ActEntity<Leave> {
    private static final long serialVersionUID = -7436137115943178295L;
    private SysUser user;    //	归属用户
    private SysDept dept;    //	归属部门
    private String post;//岗位
    private String content;// 请假原因
    private Date startTime;    // 请假开始日期
    private Date endTime;    // 请假结束日期
    private String leaveType;    // 请假类型

    private String exeDate;//	执行时间
    private String deptText;// 部门经理意见
    private String hrText;    // HR意见
    private String zjlText;// 总经理意见

    public SysUser getUser() {
        return user;
    }

    public void setUser(SysUser user) {
        this.user = user;
    }

    public SysDept getDept() {
        return dept;
    }

    public void setDept(SysDept dept) {
        this.dept = dept;
    }

    public String getPost() {
        return post;
    }

    public void setPost(String post) {
        this.post = post;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    public Date getStartTime() {
        return startTime;
    }

    public void setStartTime(Date startTime) {
        this.startTime = startTime;
    }

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    public Date getEndTime() {
        return endTime;
    }

    public void setEndTime(Date endTime) {
        this.endTime = endTime;
    }

    public String getLeaveType() {
        return leaveType;
    }

    public void setLeaveType(String leaveType) {
        this.leaveType = leaveType;
    }

    public String getExeDate() {
        return exeDate;
    }

    public void setExeDate(String exeDate) {
        this.exeDate = exeDate;
    }

    public String getDeptText() {
        return deptText;
    }

    public void setDeptText(String deptText) {
        this.deptText = deptText;
    }

    public String getHrText() {
        return hrText;
    }

    public void setHrText(String hrText) {
        this.hrText = hrText;
    }

    public String getZjlText() {
        return zjlText;
    }

    public void setZjlText(String zjlText) {
        this.zjlText = zjlText;
    }

}
