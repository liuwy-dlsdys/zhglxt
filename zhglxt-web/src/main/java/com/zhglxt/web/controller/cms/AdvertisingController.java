package com.zhglxt.web.controller.cms;

import com.zhglxt.cms.entity.Advertising;
import com.zhglxt.cms.service.IAdvertisingService;
import com.zhglxt.cms.service.ISiteService;
import com.zhglxt.common.annotation.Log;
import com.zhglxt.common.core.controller.BaseController;
import com.zhglxt.common.core.entity.AjaxResult;
import com.zhglxt.common.core.page.TableDataInfo;
import com.zhglxt.common.core.text.Convert;
import com.zhglxt.common.enums.BusinessType;
import com.zhglxt.common.util.ShiroUtils;
import com.zhglxt.common.util.WebUtil;
import com.zhglxt.common.util.uuid.UUID;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PostMapping;
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
@Api(tags = "企业官网-广告管理")
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

    @ApiOperation(value = "广告列表")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "id", value = "广告id",dataType = "String", paramType = "query", dataTypeClass = String.class),
            @ApiImplicitParam(name = "title", value = "广告标题",dataType = "String", paramType = "query", dataTypeClass = String.class)
    })
    @PostMapping("/list")
    @ResponseBody
    public TableDataInfo selectAdvertisingList(HttpServletRequest request) {
        Map<String, Object> paramMap = WebUtil.paramsToMap(request.getParameterMap());
        paramMap.put("siteId", siteService.selectOneSite().getId());
        startPage();
        List<Advertising> advertisingList = advertisingService.selectAdvertisingList(paramMap);
        return getDataTable(advertisingList);
    }

    @RequestMapping("/add")
    public String add(HttpServletRequest request, Model model) {
        Map<String, Object> paramMap = WebUtil.paramsToMap(request.getParameterMap());
        List<Advertising> advertisings = advertisingService.selectAdvertisingList(paramMap);
        model.addAttribute("advertising", advertisings.get(0));
        return prefix + "/addAdvertising";
    }

    @RequestMapping("/edit")
    public String edit(HttpServletRequest request, Model model) {
        Map<String, Object> paramMap = WebUtil.paramsToMap(request.getParameterMap());
        List<Advertising> advertisings = advertisingService.selectAdvertisingList(paramMap);
        model.addAttribute("advertising", advertisings.get(0));
        return prefix + "/editAdvertising";
    }

    /**
     * 广告管理-新增
     */
    @Log(title = "CMS-广告管理-新增", businessType = BusinessType.INSERT)
    @RequestMapping("/addAdvertising")
    @ResponseBody
    public AjaxResult addAdvertising(HttpServletRequest request) {
        Map<String, Object> paramMap = WebUtil.paramsToMap(request.getParameterMap());

            paramMap.put("id", UUID.fastUUID().toString(true));
            paramMap.put("siteId", siteService.selectOneSite().getId());
            paramMap.put("createBy", ShiroUtils.getLoginName());
            paramMap.put("updateBy", ShiroUtils.getLoginName());
            return toAjax(advertisingService.insertAdvertising(paramMap));

    }

    /**
     * 广告管理-编辑
     */
    @Log(title = "CMS-广告管理-编辑", businessType = BusinessType.INSERT)
    @RequestMapping("/editAdvertising")
    @ResponseBody
    public AjaxResult editAdvertising(HttpServletRequest request) {
        Map<String, Object> paramMap = WebUtil.paramsToMap(request.getParameterMap());

        paramMap.put("siteId", siteService.selectOneSite().getId());
        paramMap.put("updateBy", ShiroUtils.getLoginName());
        return toAjax(advertisingService.updateAdvertising(paramMap));

    }

    /**
     * 删除广告
     */
    @ApiOperation(value = "删除广告")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "ids", value = "广告id列表（如：1,2,3,4）", required = true, dataType = "String", paramType = "query", dataTypeClass = String.class)
    })
    @Log(title = "CMS-广告管理-删除", businessType = BusinessType.DELETE)
    @PostMapping("/remove")
    @ResponseBody
    public AjaxResult deleteAdvertising(String ids) {
        return toAjax(advertisingService.deleteAdvertising(Convert.toStrArray(ids)));
    }

}
