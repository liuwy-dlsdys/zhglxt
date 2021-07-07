package com.zhglxt.cms.mapper;

import com.zhglxt.cms.entity.Advertising;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;

/**
 * 广告管理 数据层
 *
 * @author liuwy
 * @date 2019/12/15
 */
@Mapper
@Component
public interface AdvertisingMapper {
    /**
     * 查询广告列表
     */
    public List<Advertising> selectAdvertisingList(Map<String, Object> paramMap);

    /**
     * 新增广告
     *
     * @param paramMap
     * @return
     */
    public int insertAdvertising(Map<String, Object> paramMap);

    /**
     * 编辑广告
     *
     * @param paramMap
     * @return
     */
    public int updateAdvertising(Map<String, Object> paramMap);

    /**
     * 删除广告
     *
     * @param ids
     * @return
     */
    public int deleteAdvertisingById(String[] ids);

    /**
     * 根据站点id删除广告
     *
     * @param siteIds
     * @return
     */
    public int deleteAdvertisingBySiteIds(String[] siteIds);
}
