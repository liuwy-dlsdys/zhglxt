package com.zhglxt.web.controller.cms;


import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.zhglxt.cms.entity.Advertising;
import com.zhglxt.cms.entity.Article;
import com.zhglxt.cms.entity.Column;
import com.zhglxt.cms.entity.Site;
import com.zhglxt.cms.mapper.ArticleMapper;
import com.zhglxt.cms.service.IAdvertisingService;
import com.zhglxt.cms.service.IColumnService;
import com.zhglxt.cms.service.ISiteService;
import com.zhglxt.common.core.controller.BaseController;
import com.zhglxt.common.util.WebUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Map;

/**
 * @Description 官网Index控制层
 * @Author liuwy
 * @Date 2019/12/4
 */
@Controller
@RequestMapping("/cms")
public class IndexController extends BaseController {
    private String prefix = "cms";

    //状态默认显示
    private String status = "0";

    @Autowired
    private ISiteService siteService;

    @Autowired
    private IColumnService columnService;

    @Autowired
    private IAdvertisingService advertisingService;

    @Autowired
    private ArticleMapper articleMapper;

    /**
     * 初始化官网栏目数据
     */
    @RequestMapping("/index.html")
    public String cmsMain(HttpServletRequest request, Model model) {
        //站点
        Site site = siteService.selectOneSite();
        model.addAttribute("site", site);

        //查询出当前站点顶级栏目的栏目id
        Map<String, Object> rootColumnMap = Maps.newHashMap();
        rootColumnMap.put("siteId", site.getId());
        rootColumnMap.put("parentId", 0);
        List<Column> rootColumn = columnService.selectColumnList(rootColumnMap);

        //栏目
        Map<String, Object> paramMap = WebUtil.paramsToMap(request.getParameterMap());
        paramMap.put("parentId", rootColumn.get(0).getId());//父id为当前站点，顶级栏目id
        paramMap.put("display", status);//0：显示的
        paramMap.put("siteId", site.getId());//站点id
        List<Column> columns = columnService.selectCMSColumnList(paramMap);
        model.addAttribute("columns", columns);

        //广告轮播图
        Map<String, Object> advertisingsParamMap = Maps.newHashMap();
        advertisingsParamMap.put("siteId", site.getId());//站点id
        advertisingsParamMap.put("status", status);//0显示 1隐藏
        List<Advertising> advertisings = advertisingService.selectAdvertisingList(advertisingsParamMap);
        model.addAttribute("advertisings", advertisings);

        //解决方案
        Map<String, Object> articleParamMap = Maps.newHashMap();
        articleParamMap.put("columnFlag", "solution");//解决方案
        articleParamMap.put("status", status);//0显示 1隐藏
        articleParamMap.put("siteId", site.getId());//站点id
        List<Article> articles = articleMapper.selectArticleList(articleParamMap);
        List<Article> articleList = Lists.newArrayList();
        if (!CollectionUtils.isEmpty(articles)) {
            if (articles.size() > 6) {
                //解决方案大于6个时，只显示6个
                articleList = articles.subList(0, 6);
            }
        }
        if (!CollectionUtils.isEmpty(articleList)) {
            model.addAttribute("solutions", articleList);
        } else {
            model.addAttribute("solutions", articles);
        }

        //新闻动态
        Map<String, Object> newsParamMap = Maps.newHashMap();
        newsParamMap.put("columnFlag", "news");//新闻动态
        newsParamMap.put("status", status);//0显示 1隐藏
        newsParamMap.put("siteId", site.getId());//站点id
        List<Article> newsList = articleMapper.selectArticleList(newsParamMap);
        List<Article> newsListTemp = Lists.newArrayList();
        if (!CollectionUtils.isEmpty(newsList)) {
            if (newsList.size() > 3) {
                //新闻动态大于3个时，只显示3个
                newsListTemp = newsList.subList(0, 3);
            }
        }
        if (!CollectionUtils.isEmpty(newsListTemp)) {
            model.addAttribute("newss", newsListTemp);
        } else {
            model.addAttribute("newss", newsList);
        }

        //企业发展史
        Map<String, Object> developHistoryParamMap = Maps.newHashMap();
        developHistoryParamMap.put("columnFlag", "developHistory");//企业发展史
        developHistoryParamMap.put("status", status);//0显示 1隐藏
        developHistoryParamMap.put("siteId", site.getId());//站点id
        List<Article> developHistoryList = articleMapper.selectArticleList(developHistoryParamMap);
        model.addAttribute("developHistorys", developHistoryList);


        return prefix + "/cmsMainIndex";
    }

    @RequestMapping("/{url}.html")
    public String columnTabData(@PathVariable String url, HttpServletRequest request, Model model) {
        //站点
        Site site = siteService.selectOneSite();
        model.addAttribute("site", site);

        //查询出当前站点顶级栏目的栏目id
        Map<String, Object> rootColumnMap = Maps.newHashMap();
        rootColumnMap.put("siteId", site.getId());
        rootColumnMap.put("parentId", 0);
        List<Column> rootColumn = columnService.selectColumnList(rootColumnMap);

        Map<String, Object> paramMap = WebUtil.paramsToMap(request.getParameterMap());
        paramMap.put("parentId", rootColumn.get(0).getId());//父id为当前站点，顶级栏目id
        paramMap.put("display", status);//0：显示的
        paramMap.put("siteId", site.getId());//站点id
        List<Column> columns = columnService.selectCMSColumnList(paramMap);
        model.addAttribute("columns", columns);

        Map<String, Object> tabParamMap = Maps.newHashMap();
        tabParamMap.put("columnFlag", url);
        tabParamMap.put("status", status);//0显示 1隐藏
        tabParamMap.put("siteId", site.getId());//站点id
        List<Article> tabList = articleMapper.selectArticleList(tabParamMap);
        model.addAttribute(url + "s", tabList);

        return prefix + "/cmsMainIndex";
    }
}
