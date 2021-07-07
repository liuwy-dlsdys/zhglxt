package com.zhglxt.cms.service;

import com.zhglxt.cms.entity.Article;

import java.util.List;
import java.util.Map;

/**
 * 文章 业务层
 * @author liuwy
 * @date 2019/12/18
 */
public interface IArticleService {
    /**
     * 查询文章列表
     *
     * @param paramMap
     * @return
     */
    public List<Article> selectArticleList(Map<String, Object> paramMap);

    /**
     * 新增文章
     *
     * @param paramMap
     * @return
     */
    public int addArticle(Map<String, Object> paramMap);

    /**
     * 删除文章
     *
     * @param ids
     * @return
     */
    public int deleteArticle(String[] ids);

    /**
     * 修改文章
     *
     * @param paramMap
     * @return
     */
    int updateArticle(Map<String, Object> paramMap);
}
