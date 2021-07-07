package com.zhglxt.oa.entity;

import com.zhglxt.common.core.entity.BaseEntity;

import java.util.List;

/**
 * @Description 通知通告实体
 * @Author liuwy
 * @Date 2020/12/19
 */
public class Notify extends BaseEntity {
    private static final long serialVersionUID = 608892740078533759L;
    private String id;            // 主键id
    private String notifyType;    // 类型
    private String notifyTitle;    // 标题
    private String notifyContent; // 内容
    private String files;        // 附件
    private String status;        // 状态
    private String readNum;        // 已读
    private String unReadNum;    // 未读
    private boolean isSelf;        // 是否只查询自己的通知
    private String readFlag;    // 本人阅读状态
    private String delFlag;    // 删除标记（0：正常；1：删除；2：审核）

    private String userId;        //用户id
    private List<String> userIds; //用户ids
    private List<String> deptUserIds; //部门用户ids

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getNotifyType() {
        return notifyType;
    }

    public void setNotifyType(String notifyType) {
        this.notifyType = notifyType;
    }

    public String getNotifyTitle() {
        return notifyTitle;
    }

    public void setNotifyTitle(String notifyTitle) {
        this.notifyTitle = notifyTitle;
    }

    public String getNotifyContent() {
        return notifyContent;
    }

    public void setNotifyContent(String notifyContent) {
        this.notifyContent = notifyContent;
    }

    public String getFiles() {
        return files;
    }

    public void setFiles(String files) {
        this.files = files;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getReadNum() {
        return readNum;
    }

    public void setReadNum(String readNum) {
        this.readNum = readNum;
    }

    public String getUnReadNum() {
        return unReadNum;
    }

    public void setUnReadNum(String unReadNum) {
        this.unReadNum = unReadNum;
    }

    public boolean isSelf() {
        return isSelf;
    }

    public void setSelf(boolean self) {
        isSelf = self;
    }

    public String getReadFlag() {
        return readFlag;
    }

    public void setReadFlag(String readFlag) {
        this.readFlag = readFlag;
    }

    public String getDelFlag() {
        return delFlag;
    }

    public void setDelFlag(String delFlag) {
        this.delFlag = delFlag;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public List<String> getUserIds() {
        return userIds;
    }

    public void setUserIds(List<String> userIds) {
        this.userIds = userIds;
    }

    public List<String> getDeptUserIds() {
        return deptUserIds;
    }

    public void setDeptUserIds(List<String> deptUserIds) {
        this.deptUserIds = deptUserIds;
    }
}
