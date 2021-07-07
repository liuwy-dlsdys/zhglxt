package com.zhglxt.cms.service;

import com.zhglxt.cms.entity.Advertising;

import java.util.List;
import java.util.Map;

/**
 * 广告管理 业务层
 * @author liuwy
 * @date 2019/12/15
 */
public interface IAdvertisingService {
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
    public int deleteAdvertising(String[] ids);
}
