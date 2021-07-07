package com.zhglxt.cms.entity;

import java.io.Serializable;
import java.util.Date;

/**
 * 站点实体类
 * @Author liuwy
 * @Date 2021/1/5
 */
public class Site implements Serializable {
    private static final long serialVersionUID = -1148337436333825189L;
    private String id;//主键id
    private String name;//名称
    private String title;//标题
    private String description;//描述
    private String keywords;//关键字
    private String copyright;//版权信息
    private String siteDomain;//站点域名
    private String bgMusic;//背景音乐
    private String sort;//排序
    private String status;//0.显示、1.隐藏
    private String createBy;//创建人
    private Date createTime;//创建时间
    private String updateBy;//更新人
    private Date updateTime;//更新时间
    private String remark;//备注

    /**
     * 栏目管理-栏目列表-新增、编辑 所属站点是否存在 默认不存在
     */
    private boolean flag = false;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getKeywords() {
        return keywords;
    }

    public void setKeywords(String keywords) {
        this.keywords = keywords;
    }

    public String getCopyright() {
        return copyright;
    }

    public void setCopyright(String copyright) {
        this.copyright = copyright;
    }

    public String getSiteDomain() {
        return siteDomain;
    }

    public void setSiteDomain(String siteDomain) {
        this.siteDomain = siteDomain;
    }

    public String getBgMusic() {
        return bgMusic;
    }

    public void setBgMusic(String bgMusic) {
        this.bgMusic = bgMusic;
    }

    public String getSort() {
        return sort;
    }

    public void setSort(String sort) {
        this.sort = sort;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getCreateBy() {
        return createBy;
    }

    public void setCreateBy(String createBy) {
        this.createBy = createBy;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public String getUpdateBy() {
        return updateBy;
    }

    public void setUpdateBy(String updateBy) {
        this.updateBy = updateBy;
    }

    public Date getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public boolean isFlag() {
        return flag;
    }

    public void setFlag(boolean flag) {
        this.flag = flag;
    }
}
