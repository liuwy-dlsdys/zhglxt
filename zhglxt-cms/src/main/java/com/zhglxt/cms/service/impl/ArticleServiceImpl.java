package com.zhglxt.cms.service.impl;


import com.google.common.collect.Maps;
import com.zhglxt.cms.entity.Article;
import com.zhglxt.cms.mapper.ArticleMapper;
import com.zhglxt.cms.service.IArticleService;
import com.zhglxt.cms.service.ISiteService;
import com.zhglxt.common.util.ShiroUtils;
import com.zhglxt.common.util.StringUtils;
import com.zhglxt.common.util.uuid.IdUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;

/**
 * 文章 业务层处理
 * @author liuwy
 * @date 2019/12/18
 */
@Service
public class ArticleServiceImpl implements IArticleService {
    @Autowired
    private ArticleMapper articleMapper;

    @Autowired
    private ISiteService siteService;

    @Override
    public List<Article> selectArticleList(Map<String, Object> paramMap) {
        return articleMapper.selectArticleList(paramMap);
    }

    @Override
    @Transactional
    public int addArticle(Map<String, Object> paramMap) {
        String articleId = IdUtils.fastSimpleUUID();
        paramMap.put("id", articleId);
        paramMap.put("siteId", siteService.selectOneSite().getId());
        paramMap.put("status", "0");//0：正常  1：删除
        paramMap.put("createBy", ShiroUtils.getLoginName());
        paramMap.put("updateBy", ShiroUtils.getLoginName());

        //保存文章内容关联表数据
        Map<String, Object> contentParamMap = Maps.newHashMap();
        contentParamMap.put("id", IdUtils.fastSimpleUUID());
        contentParamMap.put("articleId", articleId);
        contentParamMap.put("content", paramMap.get("content"));
        articleMapper.addArticleContent(contentParamMap);

        return articleMapper.addArticle(paramMap);
    }

    @Override
    @Transactional
    public int deleteArticle(String[] ids) {
        //删除文章内容关联表数据
        articleMapper.deleteArticleContentByArticleIds(ids);
        return articleMapper.deleteArticle(ids);
    }

    @Override
    @Transactional
    public int updateArticle(Map<String, Object> paramMap) {
        //修改文章内容关联表数据
        Map<String, Object> contentParamMap = Maps.newHashMap();
        contentParamMap.put("articleId", paramMap.get("id"));
        contentParamMap.put("content", paramMap.get("content"));
        articleMapper.updateArticleContent(contentParamMap);

        paramMap.put("updateBy", ShiroUtils.getLoginName());
        return articleMapper.updateArticle(paramMap);
    }
}
