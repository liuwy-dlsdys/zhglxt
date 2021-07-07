package com.zhglxt.cms.mapper;

import com.zhglxt.cms.entity.Site;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;

/**
 * @Description 站点管理 数据层
 * @Author liuwy
 * @Date 2021/1/5
 */
@Mapper
@Component
public interface SiteMapper {
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
}
