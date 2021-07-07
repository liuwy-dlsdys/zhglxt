package com.zhglxt.cms.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.zhglxt.common.core.entity.Ztree;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

/**
 * 栏目实体类
 * @author liuwy
 * @date 2019/12/3
 */
public class Column extends Ztree implements Serializable {
    private static final long serialVersionUID = 994025886656464148L;
    private String siteId;//站点编号
    private String siteName;//站点名称
    private String upColumnName;//上级栏目名称
    private String columnCode;//栏目编码
    private String columnFlag;//栏目标识
    private String status;//状态:0：正常、1：删除
    private String display;//是否显示 0：显示 1：隐藏
    private String outLink;//是否外部链接 0：是 1：否
    private String href;//外部链接访问地址
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date createTime;//创建时间
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date updateTime;//更新时间
    private String sort;//排序
    private String remark;//备注
    private List<Column> columnChilds;//子分类

    public String getSiteId() {
        return siteId;
    }

    public void setSiteId(String siteId) {
        this.siteId = siteId;
    }

    public String getSiteName() {
        return siteName;
    }

    public void setSiteName(String siteName) {
        this.siteName = siteName;
    }

    public String getUpColumnName() {
        return upColumnName;
    }

    public void setUpColumnName(String upColumnName) {
        this.upColumnName = upColumnName;
    }

    public String getColumnCode() {
        return columnCode;
    }

    public void setColumnCode(String columnCode) {
        this.columnCode = columnCode;
    }

    public String getColumnFlag() {
        return columnFlag;
    }

    public void setColumnFlag(String columnFlag) {
        this.columnFlag = columnFlag;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getDisplay() {
        return display;
    }

    public void setDisplay(String display) {
        this.display = display;
    }

    public String getOutLink() {
        return outLink;
    }

    public void setOutLink(String outLink) {
        this.outLink = outLink;
    }

    public String getHref() {
        return href;
    }

    public void setHref(String href) {
        this.href = href;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public Date getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
    }

    public String getSort() {
        return sort;
    }

    public void setSort(String sort) {
        this.sort = sort;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public List<Column> getColumnChilds() {
        return columnChilds;
    }

    public void setColumnChilds(List<Column> columnChilds) {
        this.columnChilds = columnChilds;
    }
}
