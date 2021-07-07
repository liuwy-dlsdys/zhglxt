package com.zhglxt.cms.service.impl;


import com.zhglxt.cms.entity.Advertising;
import com.zhglxt.cms.mapper.AdvertisingMapper;
import com.zhglxt.cms.service.IAdvertisingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;

/**
 * 广告管理 业务层处理
 * @author liuwy
 * @date 2019/12/15
 */
@Service
public class AdvertisingServiceImpl implements IAdvertisingService {
    @Autowired
    private AdvertisingMapper advertisingMapper;

    @Override
    public List<Advertising> selectAdvertisingList(Map<String, Object> paramMap) {
        return advertisingMapper.selectAdvertisingList(paramMap);
    }

    @Override
    @Transactional
    public int insertAdvertising(Map<String, Object> paramMap) {
        return advertisingMapper.insertAdvertising(paramMap);
    }

    @Override
    @Transactional
    public int updateAdvertising(Map<String, Object> paramMap) {
        return advertisingMapper.updateAdvertising(paramMap);
    }

    @Override
    @Transactional
    public int deleteAdvertising(String[] ids) {
        return advertisingMapper.deleteAdvertisingById(ids);
    }
}
