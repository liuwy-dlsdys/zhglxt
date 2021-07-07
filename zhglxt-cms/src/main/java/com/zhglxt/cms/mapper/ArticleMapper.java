package com.zhglxt.cms.mapper;

import com.zhglxt.cms.entity.Article;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;

/**
 * 文章 数据层
 *
 * @author liuwy
 * @date 2019/12/18
 */
@Mapper
@Component
public interface ArticleMapper {

    /**
     * 查询文章列表
     *
     * @param paramMap
     * @return
     */
    List<Article> selectArticleList(Map<String, Object> paramMap);

    /**
     * 新增文章
     *
     * @param paramMap
     * @return
     */
    int addArticle(Map<String, Object> paramMap);

    /**
     * 删除文章
     *
     * @param ids
     * @return
     */
    int deleteArticle(String[] ids);

    /**
     * 删除文章内容关联表的文章数据
     *
     * @param ids
     * @return
     */
    void deleteArticleContentByArticleIds(String[] ids);

    /**
     * 新增文章内容关联表数据
     *
     * @param contentParamMap
     * @return
     */
    void addArticleContent(Map<String, Object> contentParamMap);

    /**
     * 更新文章内容
     *
     * @param paramMap
     * @return
     */
    int updateArticle(Map<String, Object> paramMap);

    /**
     * 更新文章内容关联表数据
     *
     * @param contentParamMap
     * @return
     */
    int updateArticleContent(Map<String, Object> contentParamMap);

    /**
     * 根据站点id查询文章列表
     * @param siteIds
     * @return
     */
    List<Article> selectArticleListBySiteIds(String[] siteIds);

    /**
     * 根据站点id删除文章
     * @param siteIds
     * @return
     */
    void deleteArticleBySiteIds(String[] siteIds);
}
