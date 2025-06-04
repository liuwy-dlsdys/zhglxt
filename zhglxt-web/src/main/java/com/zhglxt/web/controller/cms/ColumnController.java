package com.zhglxt.web.controller.cms;

import com.zhglxt.cms.entity.Article;
import com.zhglxt.cms.entity.Column;
import com.zhglxt.cms.service.IColumnService;
import com.zhglxt.cms.service.ISiteService;
import com.zhglxt.cms.service.impl.ArticleServiceImpl;
import com.zhglxt.common.annotation.Log;
import com.zhglxt.common.core.controller.BaseController;
import com.zhglxt.common.core.entity.AjaxResult;
import com.zhglxt.common.enums.BusinessType;
import com.zhglxt.common.utils.ShiroUtils;
import com.zhglxt.common.utils.StringUtils;
import com.zhglxt.common.utils.WebUtil;
import com.zhglxt.common.utils.uuid.UUID;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Map;

/**
 * 栏目Controller
 * @author liuwy
 * @date 2019/12/3
 */
@Api(tags ="企业官网-栏目管理")
@Controller
@RequestMapping("/cms/column")
public class ColumnController extends BaseController {
    private String prefix = "cms/column";

    @Autowired
    private IColumnService columnService;

    @Autowired
    private ArticleServiceImpl articleService;

    @Autowired
    private ISiteService siteService;

    @RequestMapping("/index")
    public String mainIndex() {
        return prefix + "/columnIndex";
    }

    @ApiOperation(value = "栏目列表")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "columnId", value = "栏目id", dataType = "String", paramType = "query", dataTypeClass = String.class),
            @ApiImplicitParam(name = "columnName", value = "栏目名称", dataType = "String", paramType = "query", dataTypeClass = String.class)
    })
    @PostMapping("/columnList")
    @ResponseBody
    public List<Column> columnList(HttpServletRequest request) {
        Map<String, Object> paramMap = WebUtil.paramsToMap(request.getParameterMap());
        paramMap.put("siteId", siteService.selectOneSite().getId());
        List<Column> columns = columnService.selectColumnList(paramMap);
        return columns;
    }

    /**
     * 选择栏目树
     */
    @GetMapping("/selectColumnTree")
    public String selectColumnTree(HttpServletRequest request, Model model) {
        Map<String, Object> paramMap = WebUtil.paramsToMap(request.getParameterMap());
        paramMap.put("siteId", siteService.selectOneSite().getId());
        List<Column> columns = columnService.selectColumnList(paramMap);
        model.addAttribute("column", CollectionUtils.isEmpty(columns) ? new Column() : columns.get(0));
        return prefix + "/columnTree";
    }

    /**
     * 加载 栏目树
     */
    @RequestMapping("/columnTreeData")
    @ResponseBody
    public List<Column> columnTreeData(HttpServletRequest request) {
        Map<String, Object> paramMap = WebUtil.paramsToMap(request.getParameterMap());
        paramMap.put("siteId", siteService.selectOneSite().getId());
        List<Column> columns = columnService.selectColumnList(paramMap);
        return columns;
    }

    @RequestMapping("/add")
    public String add(HttpServletRequest request, Model model) {
        Map<String, Object> paramMap = WebUtil.paramsToMap(request.getParameterMap());
        paramMap.put("siteId", siteService.selectOneSite().getId());
        if(paramMap.containsKey("id")){
            if(StringUtils.isNotEmpty(paramMap.get("id").toString())){
                if("0".equals(paramMap.get("id").toString())){
                    paramMap.put("parentId", 0);
                }else{
                    paramMap.put("columnId", paramMap.get("id"));
                }
            }
        }
        List<Column> columns = columnService.selectColumnList(paramMap);
        model.addAttribute("column", columns.get(0));
        return prefix + "/addColumn";
    }

    @RequestMapping("/edit")
    public String edit(HttpServletRequest request, Model model) {
        Map<String, Object> paramMap = WebUtil.paramsToMap(request.getParameterMap());
        List<Column> columns = columnService.selectColumnList(paramMap);
        model.addAttribute("column", columns.get(0));
        return prefix + "/editColumn";
    }

    /**
     * 新增栏目菜单
     */
    @SuppressWarnings("unchecked")
    @Log(title = "CMS-栏目菜单管理-新增", businessType = BusinessType.INSERT)
    @RequestMapping("/addColumn")
    @ResponseBody
    public AjaxResult addColumn(HttpServletRequest request) {
        Map<String, Object> paramMap = WebUtil.paramsToMap(request.getParameterMap());

        paramMap.put("id", UUID.fastUUID().toString(true));
        paramMap.put("siteId", siteService.selectOneSite().getId());
        paramMap.put("createBy", ShiroUtils.getLoginName());
        return toAjax(columnService.insertColumnMenu(paramMap));

    }

    /**
     * 编辑栏目菜单
     */
    @SuppressWarnings("unchecked")
    @Log(title = "CMS-栏目菜单管理-编辑", businessType = BusinessType.INSERT)
    @RequestMapping("/editColumn")
    @ResponseBody
    public AjaxResult editColumn(HttpServletRequest request) {
        Map<String, Object> paramMap = WebUtil.paramsToMap(request.getParameterMap());

        paramMap.put("siteId", siteService.selectOneSite().getId());
        paramMap.put("updateBy", ShiroUtils.getLoginName());
        return toAjax(columnService.updateColumn(paramMap));

    }

    /**
     * 删除菜单
     */
    @ApiOperation(value = "删除栏目")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "columnId", value = "栏目id", required = true, dataType = "String", paramType = "query", dataTypeClass = String.class)
    })
    @Log(title = "CMS-栏目菜单管理-删除", businessType = BusinessType.DELETE)
    @RequestMapping("/remove")
    @ResponseBody
    public AjaxResult deleteColumn(HttpServletRequest request) {
        Map<String, Object> paramMap = WebUtil.paramsToMap(request.getParameterMap());
        List<Column> columns = columnService.selectColumnList(paramMap);
        if (!CollectionUtils.isEmpty(columns)) {
            Column column = columns.get(0);
            //非外部链接
            if (column.getOutLink().equals("1")) {
                List<Article> articleList = articleService.selectArticleList(paramMap);
                if (!CollectionUtils.isEmpty(articleList)) {
                    return error("如需删除本栏目，请先删除内容管理-文章列表中，对应本栏目的文章数据");
                }
            }
        }
        return toAjax(columnService.deleteColumn(paramMap));
    }

    /**
     * 保存栏目菜单排序
     */
    @PostMapping("/updateColumnSort")
    @ResponseBody
    public AjaxResult updateColumnSort(@RequestParam String[] menuIds, @RequestParam String[] orderNums)
    {
        columnService.updateColumnSort(menuIds, orderNums);
        return success();
    }
}
