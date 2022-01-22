package com.zhglxt.common.util;

import com.github.pagehelper.PageHelper;
import com.zhglxt.common.core.page.PageDomain;
import com.zhglxt.common.core.page.TableSupport;
import com.zhglxt.common.util.sql.SqlUtil;

/**
 * 分页工具类
 *
 * @author ruoyi
 */
public class PageUtils extends PageHelper
{
    /**
     * 设置请求分页数据
     */
    public static void startPage()
    {
        PageDomain pageDomain = TableSupport.buildPageRequest();
        Integer pageNum = pageDomain.getPageNum();
        Integer pageSize = pageDomain.getPageSize();
        if (StringUtils.isNotNull(pageNum) && StringUtils.isNotNull(pageSize))
        {
            String orderBy = SqlUtil.escapeOrderBySql(pageDomain.getOrderBy());
            Boolean reasonable = pageDomain.getReasonable();
            PageHelper.startPage(pageNum, pageSize, orderBy).setReasonable(reasonable);
        }
    }
}