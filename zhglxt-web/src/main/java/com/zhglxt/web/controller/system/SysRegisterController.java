package com.zhglxt.web.controller.system;

import com.zhglxt.common.config.GlobalConfig;
import com.zhglxt.common.core.controller.BaseController;
import com.zhglxt.common.core.entity.AjaxResult;
import com.zhglxt.common.core.entity.sys.SysUser;
import com.zhglxt.common.util.StringUtils;
import com.zhglxt.framework.shiro.service.SysRegisterService;
import com.zhglxt.system.service.ISysConfigService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * 注册验证
 *
 * @author ruoyi
 */
@Controller
public class SysRegisterController extends BaseController {
    @Autowired
    private SysRegisterService registerService;

    @Autowired
    private ISysConfigService configService;

    @GetMapping("/register")
    public String register(Model model) {
        model.addAttribute("systemName", GlobalConfig.getName());
        return "register";
    }

    @PostMapping("/register")
    @ResponseBody
    public AjaxResult ajaxRegister(SysUser user) {
        if (GlobalConfig.isDemoEnabled()) {
            return error("演示模式不允许本操作");
        }
        if (!("true".equals(configService.selectConfigByKey("sys.account.registerUser")))) {
            return error("当前系统没有开启注册功能！");
        }
        String msg = registerService.register(user);
        return StringUtils.isEmpty(msg) ? success() : error(msg);
    }
}
