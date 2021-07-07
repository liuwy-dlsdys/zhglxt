package com.zhglxt.activiti.persistence;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.zhglxt.common.core.entity.sys.SysUser;
import com.zhglxt.common.util.ShiroUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.shiro.SecurityUtils;

import java.util.Date;
import java.util.UUID;

/**
 * 数据Entity类
 * @author liuwy
 * @version 2019/9/29
 */
public abstract class DataEntity<T> extends MyBaseEntity<T> {

    protected String remarks;    // 备注
    protected SysUser createBy;    // 创建者
    protected Date createDate;    // 创建日期
    protected SysUser updateBy;    // 更新者
    protected Date updateDate;    // 更新日期
    protected String delFlag;    // 删除标记（0：正常；1：删除；2：审核）

    public DataEntity() {
        super();
        this.delFlag = DEL_FLAG_NORMAL;
    }

    public DataEntity(String id) {
        super(id);
    }

    /**
     * 插入之前执行方法，需要手动调用
     */
    @Override
    public void preInsert() {
        // 不限制ID为UUID，调用setIsNewRecord()使用自定义ID
        if (!this.isNewRecord) {
            setId(UUID.randomUUID().toString().replaceAll("-", ""));
        }
        SysUser user = (SysUser) SecurityUtils.getSubject().getPrincipal();
        if (StringUtils.isNotBlank(user.getUserId().toString())) {
            this.updateBy = user;
            this.createBy = user;
        }
        this.updateDate = new Date();
        this.createDate = this.updateDate;
    }

    /**
     * 更新之前执行方法，需要手动调用
     */
    @Override
    public void preUpdate() {
        SysUser user = (SysUser) ShiroUtils.getSysUser();
        if (StringUtils.isNotBlank(user.getUserId().toString())) {
            this.updateBy = user;
        }
        this.updateDate = new Date();
    }

    //	@Length(min=0, max=255)
    public String getRemarks() {
        return remarks;
    }

    public void setRemarks(String remarks) {
        this.remarks = remarks;
    }

    @JsonIgnore
    public SysUser getCreateBy() {
        return createBy;
    }

    public void setCreateBy(SysUser createBy) {
        this.createBy = createBy;
    }

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    public Date getCreateDate() {
        return createDate;
    }

    public void setCreateDate(Date createDate) {
        this.createDate = createDate;
    }

    @JsonIgnore
    public SysUser getUpdateBy() {
        return updateBy;
    }

    public void setUpdateBy(SysUser updateBy) {
        this.updateBy = updateBy;
    }

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    public Date getUpdateDate() {
        return updateDate;
    }

    public void setUpdateDate(Date updateDate) {
        this.updateDate = updateDate;
    }

    @JsonIgnore
//	@Length(min=1, max=1)
    public String getDelFlag() {
        return delFlag;
    }

    public void setDelFlag(String delFlag) {
        this.delFlag = delFlag;
    }

}
