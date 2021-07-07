package com.zhglxt.web.controller.demo.controller;

import com.alibaba.excel.EasyExcel;
import com.zhglxt.common.annotation.Log;
import com.zhglxt.common.config.GlobalConfig;
import com.zhglxt.common.core.controller.BaseController;
import com.zhglxt.common.core.entity.AjaxResult;
import com.zhglxt.common.core.entity.sys.SysUser;
import com.zhglxt.common.core.page.TableDataInfo;
import com.zhglxt.common.enums.BusinessType;
import com.zhglxt.common.util.poi.ExcelUtil;
import com.zhglxt.demo.entity.ImportSysUser;
import com.zhglxt.demo.excel.template.ImportSysUserTemplate;
import com.zhglxt.demo.mapper.ImportDemoMapper;
import com.zhglxt.demo.service.impl.ImportDemoServiceImpl;
import com.zhglxt.web.controller.demo.excel.EasyExcelListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

/**
 * @Description 大数据（百万级）导入Demo
 * @Author liuwy
 * @Date 2021/6/17
 */
@Controller
@RequestMapping("/demo/import")
public class ImportDemoController extends BaseController {
    private String prefix = "demo/import";

    @Autowired
    private ImportDemoServiceImpl importDemoService;
    @Autowired
    private ImportDemoMapper importDemoMapper;

    @RequestMapping("/index")
    public String importIndex() {
        return prefix + "/importIndex";
    }

    /**
     * 查询数据
     */
    @PostMapping("/list")
    @ResponseBody
    public TableDataInfo list(ImportSysUser user) {
        startPage();
        List<ImportSysUser> list = importDemoService.selectUserList(user);
        return getDataTable(list);
    }

    @Log(title = "导入Demo模板下载")
    @GetMapping("/importTemplate")
    @ResponseBody
    public AjaxResult importTemplate() {
        ExcelUtil<ImportSysUser> util = new ExcelUtil<ImportSysUser>(ImportSysUser.class);
        return util.importTemplateExcel("导入Demo模板");
    }

    @Log(title = "导入Demo数据", businessType = BusinessType.IMPORT)
    @PostMapping("/importData")
    @ResponseBody
    public AjaxResult importData(MultipartFile file, boolean updateSupport) throws Exception {
        if (GlobalConfig.isDemoEnabled()) {
            return error("演示模式不允许本操作");
        }
        EasyExcel.read(file.getInputStream(), ImportSysUserTemplate.class, new EasyExcelListener(importDemoMapper)).sheet().doRead();
        return AjaxResult.success(true);
    }

    @Log(title = "导出Demo数据", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    @ResponseBody
    public AjaxResult export(SysUser user) {
        List importSysUsers = importDemoMapper.selectUserList(new ImportSysUser());
        ExcelUtil<ImportSysUser> util = new ExcelUtil<ImportSysUser>(ImportSysUser.class);
        return util.exportExcel(importSysUsers, "测试导出用户数据");
    }

    @Log(title = "清空Demo数据", businessType = BusinessType.CLEAN)
    @PostMapping("/clean")
    @ResponseBody
    public AjaxResult clean() {
        if (GlobalConfig.isDemoEnabled()) {
            return error("演示模式不允许本操作");
        }
        importDemoMapper.clean();
        return success();
    }
}
