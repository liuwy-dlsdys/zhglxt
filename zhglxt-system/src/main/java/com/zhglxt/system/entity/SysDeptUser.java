package com.zhglxt.system.entity;

import com.zhglxt.common.core.entity.BaseEntity;

/**
 * @Description 部门用户
 * @Author liuwy
 * @Date 2021/5/14
 */
public class SysDeptUser extends BaseEntity {
    private static final long serialVersionUID = -5610833394628921242L;
    private String deptId; //部门id
    private String parentId;//部门父id
    private String status;//状态
    private String userId;//用户id
    private String userName;//用户名

    public String getDeptId() {
        return deptId;
    }

    public void setDeptId(String deptId) {
        this.deptId = deptId;
    }

    public String getParentId() {
        return parentId;
    }

    public void setParentId(String parentId) {
        this.parentId = parentId;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }
}
