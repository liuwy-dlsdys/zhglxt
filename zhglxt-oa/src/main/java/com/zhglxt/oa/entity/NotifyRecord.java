package com.zhglxt.oa.entity;

import com.zhglxt.common.core.entity.BaseEntity;

import java.util.Date;
/**
 * @Description 通知通告阅读记录实体
 * @Author liuwy
 * @Date 2020/12/19
 **/
public class NotifyRecord extends BaseEntity {
    /**
     * 主键id
     **/
    private String id;
    /**
     * 通知通告ID
     **/
    private String notifyId;
    /**
     * 接收人id
     **/
    private String userId;
    /**
     * 阅读标记（0：未读；1：已读）
     **/
    private String readFlag;
    /**
     * 阅读时间
     **/
    private Date readDate;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getNotifyId() {
        return notifyId;
    }

    public void setNotifyId(String notifyId) {
        this.notifyId = notifyId;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getReadFlag() {
        return readFlag;
    }

    public void setReadFlag(String readFlag) {
        this.readFlag = readFlag;
    }

    public Date getReadDate() {
        return readDate;
    }

    public void setReadDate(Date readDate) {
        this.readDate = readDate;
    }
}
