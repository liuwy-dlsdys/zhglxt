package com.zhglxt.web.controller.cms;

import com.zhglxt.cms.entity.Site;
import com.zhglxt.cms.service.ISiteService;
import com.zhglxt.common.annotation.Log;
import com.zhglxt.common.config.GlobalConfig;
import com.zhglxt.common.core.controller.BaseController;
import com.zhglxt.common.core.entity.AjaxResult;
import com.zhglxt.common.core.page.TableDataInfo;
import com.zhglxt.common.core.text.Convert;
import com.zhglxt.common.enums.BusinessType;
import com.zhglxt.common.util.WebUtil;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Map;

/**
 * 站点Controller
 * @author liuwy
 * @date 2019/12/3
 */
@Controller
@RequestMapping("/cms/site")
public class SiteController extends BaseController {
    private String prefix = "cms/site";

    @Autowired
    private ISiteService siteService;

    @GetMapping("index")
    public String columnList() {
        return prefix + "/siteIndex";
    }

    @RequestMapping("/list")
    @ResponseBody
    public TableDataInfo selectSiteList(HttpServletRequest request) {
        Map<String, Object> paramMap = WebUtil.paramsToMap(request.getParameterMap());
        startPage();
        List<Site> siteList = siteService.selectSiteList(paramMap);
        return getDataTable(siteList);
    }

    @GetMapping("/add")
    public String addSite() {
        return prefix + "/addSite";
    }

    /**
     * 新增站点
     */
    @Log(title = "CMS-站点管理-新增", businessType = BusinessType.INSERT)
    @RequestMapping("/addSave")
    @ResponseBody
    public AjaxResult addSave(HttpServletRequest request) {
        if (GlobalConfig.isDemoEnabled()) {
            return error("演示模式不允许本操作");
        }
        Map<String, Object> paramMap = WebUtil.paramsToMap(request.getParameterMap());
        return toAjax(siteService.insertSite(paramMap));
    }

    @RequestMapping("/edit")
    public String editSite(HttpServletRequest request, Model model) {
        Map<String, Object> paramMap = WebUtil.paramsToMap(request.getParameterMap());
        List<Site> list = siteService.selectSiteList(paramMap);
        if (!CollectionUtils.isEmpty(list)) {
            model.addAttribute("site", list.get(0));
        }
        return prefix + "/editSite";
    }

    /**
     * 修改站点
     */
    @Log(title = "CMS-站点管理--修改", businessType = BusinessType.UPDATE)
    @PostMapping("/edit")
    @ResponseBody
    public AjaxResult editSave(HttpServletRequest request) {
        if (GlobalConfig.isDemoEnabled()) {
            return error("演示模式不允许本操作");
        }
        Map<String, Object> paramMap = WebUtil.paramsToMap(request.getParameterMap());
        return toAjax(siteService.updateSite(paramMap));
    }

    /**
     * 删除站点
     */
    @Log(title = "CMS-站点管理--删除", businessType = BusinessType.DELETE)
    @RequestMapping("/remove")
    @ResponseBody
    public AjaxResult deleteSite(String ids) {
        if (GlobalConfig.isDemoEnabled()) {
            return error("演示模式不允许本操作");
        }
        return toAjax(siteService.deleteSite(Convert.toStrArray(ids)));
    }
}
