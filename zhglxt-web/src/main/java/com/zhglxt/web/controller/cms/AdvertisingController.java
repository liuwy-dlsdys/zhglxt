package com.zhglxt.web.controller.cms;

import com.zhglxt.cms.entity.Advertising;
import com.zhglxt.cms.service.IAdvertisingService;
import com.zhglxt.cms.service.ISiteService;
import com.zhglxt.common.annotation.Log;
import com.zhglxt.common.config.GlobalConfig;
import com.zhglxt.common.core.controller.BaseController;
import com.zhglxt.common.core.entity.AjaxResult;
import com.zhglxt.common.core.page.TableDataInfo;
import com.zhglxt.common.core.text.Convert;
import com.zhglxt.common.enums.BusinessType;
import com.zhglxt.common.util.ShiroUtils;
import com.zhglxt.common.util.WebUtil;
import com.zhglxt.common.util.uuid.IdUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Map;

/**
 * 广告管理Controller
 * @author liuwy
 * @date 2019/12/15
 */
@Controller
@RequestMapping("/cms/advertising")
public class AdvertisingController extends BaseController {
    private String prefix = "cms/advertising";

    @Autowired
    private IAdvertisingService advertisingService;

    @Autowired
    private ISiteService siteService;

    @RequestMapping()
    public String advertisingIndex() {
        return prefix + "/advertisingIndex";
    }

    @RequestMapping("/list")
    @ResponseBody
    public TableDataInfo selectAdvertisingList(HttpServletRequest request) {
        Map<String, Object> paramMap = WebUtil.paramsToMap(request.getParameterMap());
        startPage();
        paramMap.put("siteId", siteService.selectOneSite().getId());
        List<Advertising> advertisingList = advertisingService.selectAdvertisingList(paramMap);
        return getDataTable(advertisingList);
    }

    @RequestMapping("/add")
    public String addAdvertising(HttpServletRequest request, Model model) {
        Map<String, Object> paramMap = WebUtil.paramsToMap(request.getParameterMap());
        List<Advertising> advertisings = advertisingService.selectAdvertisingList(paramMap);
        model.addAttribute("advertising", advertisings.get(0));
        return prefix + "/addAdvertising";
    }

    @RequestMapping("/edit")
    public String editAdvertising(HttpServletRequest request, Model model) {
        Map<String, Object> paramMap = WebUtil.paramsToMap(request.getParameterMap());
        List<Advertising> advertisings = advertisingService.selectAdvertisingList(paramMap);
        model.addAttribute("advertising", advertisings.get(0));
        return prefix + "/editAdvertising";
    }

    /**
     * 广告管理-新增保存
     */
    @Log(title = "CMS-广告管理-新增编辑", businessType = BusinessType.INSERT)
    @RequestMapping("/addSave")
    @ResponseBody
    public AjaxResult addSave(HttpServletRequest request) {
        if (GlobalConfig.isDemoEnabled()) {
            return error("演示模式不允许本操作");
        }
        Map<String, Object> paramMap = WebUtil.paramsToMap(request.getParameterMap());

        if (paramMap.get("id") == null) {
            //新增
            paramMap.put("id", IdUtils.fastSimpleUUID());
            paramMap.put("siteId", siteService.selectOneSite().getId());//站点
            paramMap.put("status", "0");//0：正常  1：删除
            paramMap.put("createBy", ShiroUtils.getLoginName());
            paramMap.put("updateBy", ShiroUtils.getLoginName());
            return toAjax(advertisingService.insertAdvertising(paramMap));
        } else {
            //编辑
            paramMap.put("siteId", siteService.selectOneSite().getId());//站点
            paramMap.put("updateBy", ShiroUtils.getLoginName());
            return toAjax(advertisingService.updateAdvertising(paramMap));
        }

    }

    /**
     * 删除菜单
     */
    @Log(title = "CMS-广告管理-删除", businessType = BusinessType.DELETE)
    @RequestMapping("/remove")
    @ResponseBody
    public AjaxResult deleteAdvertising(String ids) {
        if (GlobalConfig.isDemoEnabled()) {
            return error("演示模式不允许本操作");
        }
        return toAjax(advertisingService.deleteAdvertising(Convert.toStrArray(ids)));
    }

}
