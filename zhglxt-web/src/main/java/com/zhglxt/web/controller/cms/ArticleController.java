package com.zhglxt.web.controller.cms;


import com.zhglxt.cms.entity.Article;
import com.zhglxt.cms.entity.Column;
import com.zhglxt.cms.service.IColumnService;
import com.zhglxt.cms.service.ISiteService;
import com.zhglxt.cms.service.impl.ArticleServiceImpl;
import com.zhglxt.common.annotation.Log;
import com.zhglxt.common.config.GlobalConfig;
import com.zhglxt.common.core.controller.BaseController;
import com.zhglxt.common.core.entity.AjaxResult;
import com.zhglxt.common.core.page.TableDataInfo;
import com.zhglxt.common.core.text.Convert;
import com.zhglxt.common.enums.BusinessType;
import com.zhglxt.common.util.WebUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Map;

/**
 * 文章Controller
 * @author liuwy
 * @date 2019/12/3
 */
@Controller
@RequestMapping("/cms/article")
public class ArticleController extends BaseController {
    private String prefix = "cms/article";

    @Autowired
    private ArticleServiceImpl articleService;

    @Autowired
    private IColumnService columnService;

    @Autowired
    private ISiteService siteService;

    @GetMapping
    public String articleList() {
        return prefix + "/articleIndex";
    }

    @RequestMapping("/list")
    @ResponseBody
    public TableDataInfo selectArticleList(HttpServletRequest request) {
        Map<String, Object> paramMap = WebUtil.paramsToMap(request.getParameterMap());
        startPage();
        paramMap.put("siteId", siteService.selectOneSite().getId());
        List<Article> list = articleService.selectArticleList(paramMap);
        return getDataTable(list);
    }

    @GetMapping("/add")
    public String addArticle(HttpServletRequest request, Model model) {
        Map<String, Object> paramMap = WebUtil.paramsToMap(request.getParameterMap());
        paramMap.put("siteId", siteService.selectOneSite().getId());
        paramMap.put("columnId", paramMap.get("id"));
        List<Column> columns = columnService.selectColumnList(paramMap);
        model.addAttribute("column", columns.get(0));
        return prefix + "/addArticle";
    }

    @GetMapping("/selectColumnTree")
    public String selectColumnTree(HttpServletRequest request, Model model) {
        Map<String, Object> paramMap = WebUtil.paramsToMap(request.getParameterMap());
        paramMap.put("siteId", siteService.selectOneSite().getId());
        List<Column> columns = columnService.selectColumnList(paramMap);
        model.addAttribute("column", columns.get(0));
        return prefix + "/columnTree";
    }

    @RequestMapping("/columnTreeData")
    @ResponseBody
    public List<Column> columnTreeData(HttpServletRequest request) {
        Map<String, Object> paramMap = WebUtil.paramsToMap(request.getParameterMap());
        paramMap.put("siteId", siteService.selectOneSite().getId());
        List<Column> columns = columnService.selectColumnList(paramMap);
        return columns;
    }

    /**
     * 新增保存文章
     */
    @Log(title = "CMS-文章管理-新增", businessType = BusinessType.INSERT)
    @RequestMapping("/addSave")
    @ResponseBody
    public AjaxResult addSave(HttpServletRequest request) {
        if (GlobalConfig.isDemoEnabled()) {
            return error("演示模式不允许本操作");
        }
        Map<String, Object> paramMap = WebUtil.paramsToMap(request.getParameterMap());
        return toAjax(articleService.addArticle(paramMap));
    }

    @RequestMapping("/edit")
    public String editArticle(HttpServletRequest request, Model model) {
        Map<String, Object> paramMap = WebUtil.paramsToMap(request.getParameterMap());
        List<Article> list = articleService.selectArticleList(paramMap);
        if (!CollectionUtils.isEmpty(list)) {
            model.addAttribute("article", list.get(0));
        }
        return prefix + "/editArticle";
    }

    /**
     * 修改文章
     */
    @Log(title = "文章管理", businessType = BusinessType.UPDATE)
    @PostMapping("/edit")
    @ResponseBody
    public AjaxResult editSave(HttpServletRequest request) {
        if (GlobalConfig.isDemoEnabled()) {
            return error("演示模式不允许本操作");
        }
        Map<String, Object> paramMap = WebUtil.paramsToMap(request.getParameterMap());
        return toAjax(articleService.updateArticle(paramMap));
    }

    /**
     * 删除文章
     */
    @Log(title = "CMS-文章管理-删除", businessType = BusinessType.DELETE)
    @RequestMapping("/remove")
    @ResponseBody
    public AjaxResult deleteArticle(String ids) {
        if (GlobalConfig.isDemoEnabled()) {
            return error("演示模式不允许本操作");
        }
        return toAjax(articleService.deleteArticle(Convert.toStrArray(ids)));
    }
}
