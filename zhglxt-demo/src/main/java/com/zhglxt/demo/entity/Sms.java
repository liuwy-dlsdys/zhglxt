package com.zhglxt.demo.entity;

import com.zhglxt.common.core.entity.BaseEntity;

/**
 * @Description 发送短信实体
 * @Author liuwy
 * @Date 2020/5/14
 */
public class Sms extends BaseEntity {
    private static final long serialVersionUID = -5825995821126493833L;
    /**
     * 手机号
     */
    private String phoneNumbers;

    /**
     * 模板参数 格式："{\"code\":\"123456\"}"
     */
    private String templateParam;

    private String outId;

    /**
     * 阿里云模板管理code
     */
    private String templateCode;

    public String getPhoneNumbers() {
        return phoneNumbers;
    }

    public void setPhoneNumbers(String phoneNumbers) {
        this.phoneNumbers = phoneNumbers;
    }

    public String getTemplateParam() {
        return templateParam;
    }

    public void setTemplateParam(String templateParam) {
        this.templateParam = templateParam;
    }

    public String getOutId() {
        return outId;
    }

    public void setOutId(String outId) {
        this.outId = outId;
    }

    public String getTemplateCode() {
        return templateCode;
    }

    public void setTemplateCode(String templateCode) {
        this.templateCode = templateCode;
    }
}
