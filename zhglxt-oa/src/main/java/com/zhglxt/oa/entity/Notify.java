package com.zhglxt.oa.entity;

import com.zhglxt.common.core.entity.BaseEntity;

import java.util.List;

/**
 * @Description 通知通告实体
 * @Author liuwy
 * @Date 2020/12/19
 */
public class Notify extends BaseEntity {

    /**
     * 主键id
     **/
    private String id;
    /**
     * 类型
     **/
    private String notifyType;
    /**
     * 标题
     **/
    private String notifyTitle;
    /**
     *  内容
     **/
    private String notifyContent;
    /**
     * 附件
     **/
    private String files;
    /**
     * 状态
     **/
    private String status;
    /**
     * 已读数
     **/
    private String readNum;
    /**
     * 未读数
     **/
    private String unReadNum;
    /**
     * 是否只查询自己的通知
     **/
    private boolean isSelf;
    /**
     * 本人阅读状态
     **/
    private String readFlag;
    /**
     * 删除标记（0：正常；1：删除；2：审核）
     **/
    private String delFlag;
    /**
     * 用户id
     **/
    private String userId;
    /**
     * 用户ids
     **/
    private List<String> userIds;
    /**
     * 部门用户ids
     **/
    private List<String> deptUserIds;

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
