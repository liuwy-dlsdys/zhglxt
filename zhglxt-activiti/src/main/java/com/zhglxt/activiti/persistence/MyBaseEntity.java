package com.zhglxt.activiti.persistence;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.github.pagehelper.Page;
import com.google.common.collect.Maps;
import com.zhglxt.common.core.entity.sys.SysUser;
import com.zhglxt.common.util.ShiroUtils;
import com.zhglxt.common.util.StringUtils;
import org.apache.commons.lang3.builder.ReflectionToStringBuilder;

import javax.xml.bind.annotation.XmlTransient;
import java.io.Serializable;
import java.util.Map;

/**
 * Entity支持类
 * @author liuwy
 * @version 2019/9/29
 */
public abstract class MyBaseEntity<T> implements Serializable {


    private static final long serialVersionUID = -2778061305223809336L;
    /**
     * 实体编号（唯一标识）
     */
    protected String id;

    /**
     * 当前用户
     */
    protected SysUser currentUser;

    /**
     * 当前实体分页对象
     */
    protected Page<T> page;

    /**
     * 自定义SQL（SQL标识，SQL内容）
     */
    protected Map<String, String> sqlMap;

    /**
     * 是否是新记录（默认：false），调用setIsNewRecord()设置新记录，使用自定义ID。
     * 设置为true后强制执行插入语句，ID不会自动生成，需从手动传入。
     */
    protected boolean isNewRecord = false;

    public MyBaseEntity() {

    }

    public MyBaseEntity(String id) {
        this();
        this.id = id;
    }

    //	@SupCol(isUnique="true", isHide="true")
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    @JsonIgnore
    @XmlTransient
    public SysUser getCurrentUser() {
        if (currentUser == null) {
            currentUser = ShiroUtils.getSysUser();
        }
        return currentUser;
    }

    public void setCurrentUser(SysUser currentUser) {
        this.currentUser = currentUser;
    }

    @JsonIgnore
    @XmlTransient
    public Page<T> getPage() {
        if (page == null) {
            page = new Page<T>();
        }
        return page;
    }

    public Page<T> setPage(Page<T> page) {
        this.page = page;
        return page;
    }

    @JsonIgnore
    @XmlTransient
    public Map<String, String> getSqlMap() {
        if (sqlMap == null) {
            sqlMap = Maps.newHashMap();
        }
        return sqlMap;
    }

    public void setSqlMap(Map<String, String> sqlMap) {
        this.sqlMap = sqlMap;
    }

    /**
     * 插入之前执行方法，子类实现
     */
    public abstract void preInsert();

    /**
     * 更新之前执行方法，子类实现
     */
    public abstract void preUpdate();

    /**
     * 是否是新记录（默认：false），调用setIsNewRecord()设置新记录，使用自定义ID。
     * 设置为true后强制执行插入语句，ID不会自动生成，需从手动传入。
     *
     * @return
     */
    public boolean getIsNewRecord() {
        return isNewRecord || StringUtils.isBlank(getId());
    }

    /**
     * 是否是新记录（默认：false），调用setIsNewRecord()设置新记录，使用自定义ID。
     * 设置为true后强制执行插入语句，ID不会自动生成，需从手动传入。
     */
    public void setIsNewRecord(boolean isNewRecord) {
        this.isNewRecord = isNewRecord;
    }

    /**
     * 全局变量对象
     */
//	@JsonIgnore
//	public Global getGlobal() {
//		return Global.getInstance();
//	}

    /**
     * 获取数据库名称
     */
    @JsonIgnore
    public String getDbName() {
        return "mysql";//Global.getConfig("activitidataType")
    }

    @Override
    public boolean equals(Object obj) {
        if (null == obj) {
            return false;
        }
        if (this == obj) {
            return true;
        }
        if (!getClass().equals(obj.getClass())) {
            return false;
        }
        MyBaseEntity<?> that = (MyBaseEntity<?>) obj;
        return null == this.getId() ? false : this.getId().equals(that.getId());
    }

    @Override
    public String toString() {
        return ReflectionToStringBuilder.toString(this);
    }

    /**
     * 删除标记（0：正常；1：删除；2：审核；）
     */
    public static final String DEL_FLAG_NORMAL = "0";
    public static final String DEL_FLAG_DELETE = "1";
    public static final String DEL_FLAG_AUDIT = "2";

}
