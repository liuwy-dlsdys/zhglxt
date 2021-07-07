package com.zhglxt.demo.excel.template;

import com.alibaba.excel.annotation.ExcelProperty;
import com.zhglxt.common.core.entity.BaseEntity;

import java.util.Date;

/**
 * @Description 导出模板（EasyExcel）
 * @Author liuwy 
 * @Date 2021/6/17
 **/
public class ImportSysUserTemplate extends BaseEntity {
    private static final long serialVersionUID = 1L;

    /**
     * 部门ID
     */
    @ExcelProperty(value ="部门编号")
    private String deptId;

    /**
     * 登录名称
     */
    @ExcelProperty(value ="登录名称")
    private String loginName;

    /**
     * 用户名称
     */
    @ExcelProperty(value ="用户名称")
    private String userName;

    /**
     * 用户类型
     */
    private String userType;

    /**
     * 用户邮箱
     */
    @ExcelProperty(value ="用户邮箱")
    private String email;

    /**
     * 手机号码
     */
    @ExcelProperty(value ="手机号码")
    private String phonenumber;

    /**
     * 用户性别
     */
    @ExcelProperty(value ="用户性别")
    private String sex;

    /**
     * 帐号状态（0正常 1停用）
     */
    @ExcelProperty(value ="帐号状态")
    private String status;

    /**
     * 最后登录IP
     */
    @ExcelProperty(value ="最后登录IP")
    private String loginIp;

    /**
     * 最后登录时间
     */
    @ExcelProperty(value ="最后登录时间")
    private Date loginDate;

    /**
     * 身份证号码
     */
    @ExcelProperty(value ="身份证号码")
    private String idCard;

    /**
     * 备注
     */
    @ExcelProperty(value ="备注")
    private String remark;

    public String getDeptId() {
        return deptId;
    }

    public void setDeptId(String deptId) {
        this.deptId = deptId;
    }

    public String getLoginName() {
        return loginName;
    }

    public void setLoginName(String loginName) {
        this.loginName = loginName;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getUserType() {
        return userType;
    }

    public void setUserType(String userType) {
        this.userType = userType;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhonenumber() {
        return phonenumber;
    }

    public void setPhonenumber(String phonenumber) {
        this.phonenumber = phonenumber;
    }

    public String getSex() {
        return sex;
    }

    public void setSex(String sex) {
        this.sex = sex;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getLoginIp() {
        return loginIp;
    }

    public void setLoginIp(String loginIp) {
        this.loginIp = loginIp;
    }

    public Date getLoginDate() {
        return loginDate;
    }

    public void setLoginDate(Date loginDate) {
        this.loginDate = loginDate;
    }

    public String getIdCard() {
        return idCard;
    }

    public void setIdCard(String idCard) {
        this.idCard = idCard;
    }

    @Override
    public String getRemark() {
        return remark;
    }

    @Override
    public void setRemark(String remark) {
        this.remark = remark;
    }
}
