package com.zhglxt.cms.service.impl;

import com.zhglxt.cms.entity.HelpDoc;
import com.zhglxt.cms.mapper.HelpDocMapper;
import com.zhglxt.cms.service.IHelpDocService;
import com.zhglxt.common.util.ShiroUtils;
import com.zhglxt.common.util.uuid.IdUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;

/**
 * @Description 帮助文档 业务接口-实现层
 * @Author liuwy
 * @Date 2021/6/24
 * @Version 1.0
 **/
@Service
public class HelpDocServiceImpl implements IHelpDocService {

    @Autowired
    private HelpDocMapper helpDocMapper;

    @Override
    public List<HelpDoc> selectHelpDocList(Map<String, Object> paramMap) {
        return helpDocMapper.selectHelpDocList(paramMap);
    }

    @Override
    @Transactional
    public int addHelpDoc(Map<String, Object> paramMap) {
        paramMap.put("id", IdUtils.fastSimpleUUID());
        paramMap.put("createBy", ShiroUtils.getLoginName());
        paramMap.put("updateBy",ShiroUtils.getLoginName());
        return helpDocMapper.addHelpDoc(paramMap);
    }

    @Override
    @Transactional
    public int updateHelpDoc(Map<String, Object> paramMap) {
        paramMap.put("updateBy",ShiroUtils.getLoginName());
        return helpDocMapper.updateHelpDoc(paramMap);
    }

    @Override
    @Transactional
    public int deleteHelpDoc(String[] toStrArray) {
        return helpDocMapper.deleteHelpDoc(toStrArray);
    }
}
