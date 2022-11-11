package com.zhglxt.common.core.controller;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.zhglxt.common.config.GlobalConfig;
import com.zhglxt.common.core.entity.AjaxResult;
import com.zhglxt.common.core.entity.AjaxResult.Type;
import com.zhglxt.common.core.entity.sys.SysUser;
import com.zhglxt.common.core.page.PageDomain;
import com.zhglxt.common.core.page.TableDataInfo;
import com.zhglxt.common.core.page.TableSupport;
import com.zhglxt.common.exception.DemoModeException;
import com.zhglxt.common.utils.*;
import com.zhglxt.common.utils.sql.SqlUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.ModelAttribute;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.beans.PropertyEditorSupport;
import java.io.IOException;
import java.util.Date;
import java.util.List;

/**
 * web层通用数据处理
 *
 * @author ruoyi
 */
public class BaseController {
    protected final Logger logger = LoggerFactory.getLogger(this.getClass());

    @ModelAttribute
    public void init(HttpServletRequest httpServletRequest, HttpServletResponse response) throws IOException
    {
        if (GlobalConfig.isDemoEnabled()) {
            String url = ServletUtils.getRequest().getRequestURI();

            // 需要放开的url
            if (StringUtils.isNotEmpty(url) && (url.contains("demo/import/list") || url.contains("unlockscreen") ))
            {
                return;
            }

            // 需要拦截的post请求
            if ("post".equals(httpServletRequest.getMethod().toLowerCase())) {
                if (
                        url.indexOf("add") >= 0 || url.indexOf("rule") >= 0 || url.indexOf("remove") >= 0
                                || url.indexOf("clean") >= 0 || url.indexOf("edit") >= 0 || url.indexOf("authUser/selectAll") >= 0
                                || url.indexOf("export") >= 0 || url.indexOf("change") >= 0 || url.indexOf("forceLogout") >= 0
                                || url.indexOf("update") >= 0 || url.indexOf("resetPwd") >= 0 || url.indexOf("import") >= 0
                                || url.indexOf("create") >= 0 || url.indexOf("convert") >= 0 || url.indexOf("delete") >= 0
                                || url.indexOf("save") >= 0 || url.indexOf("claim") >= 0 || url.indexOf("register") >= 0
                                || url.indexOf("cancel") >= 0 || url.indexOf("deploy") >= 0 || url.indexOf("authDataScope") >= 0
                                || url.indexOf("unlock") >= 0 || url.indexOf("batchForceLogout") >= 0
                ){
                    throw new DemoModeException();
                }
            }

            // 需要拦截的get请求
            else if ("get".equals(httpServletRequest.getMethod().toLowerCase()))
            {
                // if (url.indexOf("remove") >= 0 || url.indexOf("genCode") >= 0 || url.indexOf("batchGenCode") >= 0)
                if (
                        url.indexOf("remove") >= 0
                                || url.indexOf("/genCode") >= 0
                                || url.indexOf("deploy") >= 0
                                || url.indexOf("convertToModel") >= 0
                                || url.indexOf("updateState") >= 0
                                || url.indexOf("synchDb") >= 0
                                || url.indexOf("batchGenCode") >= 0
                ){
                    throw new DemoModeException();
                }
            }

            // 需要拦截的put请求
            else if("put".equals(httpServletRequest.getMethod().toLowerCase())){
                if (url.indexOf("save") >= 0)
                {
                    throw new DemoModeException();
                }
            }
        }
    }

    /**
     * 将前台传递过来的日期格式的字符串，自动转化为Date类型
     */
    @InitBinder
    public void initBinder(WebDataBinder binder) {
        // Date 类型转换
        binder.registerCustomEditor(Date.class, new PropertyEditorSupport() {
            @Override
            public void setAsText(String text) {
                setValue(DateUtils.parseDate(text));
            }
        });
    }

    /**
     * 设置请求分页数据
     */
    protected void startPage() {
        PageUtils.startPage();
    }

    /**
     * 设置请求排序数据
     */
    protected void startOrderBy() {
        PageDomain pageDomain = TableSupport.buildPageRequest();
        if (StringUtils.isNotEmpty(pageDomain.getOrderBy())) {
            String orderBy = SqlUtil.escapeOrderBySql(pageDomain.getOrderBy());
            PageHelper.orderBy(orderBy);
        }
    }

    /**
     * 清理分页的线程变量
     */
    protected void clearPage()
    {
        PageUtils.clearPage();
    }

    /**
     * 获取request
     */
    public HttpServletRequest getRequest() {
        return ServletUtils.getRequest();
    }

    /**
     * 获取response
     */
    public HttpServletResponse getResponse() {
        return ServletUtils.getResponse();
    }

    /**
     * 获取session
     */
    public HttpSession getSession() {
        return getRequest().getSession();
    }

    /**
     * 响应请求分页数据
     */
    @SuppressWarnings({"rawtypes", "unchecked"})
    protected TableDataInfo getDataTable(List<?> list) {
        TableDataInfo rspData = new TableDataInfo();
        rspData.setCode(0);
        rspData.setRows(list);
        rspData.setTotal(new PageInfo(list).getTotal());
        return rspData;
    }

    /**
     * 响应返回结果
     *
     * @param rows 影响行数
     * @return 操作结果
     */
    protected AjaxResult toAjax(int rows) {
        return rows > 0 ? success() : error();
    }

    /**
     * 响应返回结果
     *
     * @param result 结果
     * @return 操作结果
     */
    protected AjaxResult toAjax(boolean result) {
        return result ? success() : error();
    }

    /**
     * 返回成功
     */
    public AjaxResult success() {
        return AjaxResult.success();
    }

    /**
     * 返回失败消息
     */
    public AjaxResult error() {
        return AjaxResult.error();
    }

    /**
     * 返回成功消息
     */
    public AjaxResult success(String message) {
        return AjaxResult.success(message);
    }

    /**
     * 返回成功数据
     */
    public static AjaxResult success(Object data)
    {
        return AjaxResult.success("操作成功", data);
    }

    /**
     * 返回失败消息
     */
    public AjaxResult error(String message) {
        return AjaxResult.error(message);
    }

    /**
     * 返回错误码消息
     */
    public AjaxResult error(Type type, String message) {
        return new AjaxResult(type, message);
    }

    /**
     * 页面跳转
     */
    public String redirect(String url) {
        return StringUtils.format("redirect:{}", url);
    }

    /**
     * 获取用户缓存信息
     */
    public SysUser getSysUser()
    {
        return ShiroUtils.getSysUser();
    }

    /**
     * 设置用户缓存信息
     */
    public void setSysUser(SysUser user)
    {
        ShiroUtils.setSysUser(user);
    }

    /**
     * 获取登录用户id
     */
    public String getUserId()
    {
        return getSysUser().getUserId();
    }

    /**
     * 获取登录用户名
     */
    public String getLoginName()
    {
        return getSysUser().getLoginName();
    }
}
