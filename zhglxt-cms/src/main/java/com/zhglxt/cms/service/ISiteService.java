package com.zhglxt.cms.service;

import com.zhglxt.cms.entity.Site;

import java.util.List;
import java.util.Map;

/**
 * @Description 站点管理 业务层
 * @Author liuwy
 * @Date 2021/1/5
 */
public interface ISiteService {
    /**
     * 查询站点列表
     */
    public List<Site> selectSiteList(Map<String, Object> paramMap);

    /**
     * 新增站点
     *
     * @param paramMap
     * @return
     */
    public int insertSite(Map<String, Object> paramMap);

    /**
     * 编辑站点
     *
     * @param paramMap
     * @return
     */
    public int updateSite(Map<String, Object> paramMap);

    /**
     * 删除站点
     *
     * @param ids
     * @return
     */
    public int deleteSite(String[] ids);

    /**
     * 查询排序为 第一的 站点
     *
     * @return
     */
    Site selectOneSite();
}
