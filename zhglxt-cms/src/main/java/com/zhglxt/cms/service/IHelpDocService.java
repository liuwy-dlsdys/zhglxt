package com.zhglxt.cms.service;

import com.zhglxt.cms.entity.HelpDoc;

import java.util.List;
import java.util.Map;

/**
 * @Description 帮助文档 业务接口层
 * @Author liuwy
 * @Date 2021/6/24
 * @Version 1.0
 **/
public interface IHelpDocService {
    /**
     * 查询帮助文档列表
     */
    public List<HelpDoc> selectHelpDocList(Map<String, Object> paramMap);

    /**
     * 新增帮助文档
     **/
    int addHelpDoc(Map<String, Object> paramMap);

    /**
     * 编辑帮助文档
     **/
    int updateHelpDoc(Map<String, Object> paramMap);

    /**
     * 删除帮助文档
     **/
    int deleteHelpDoc(String[] toStrArray);
}
