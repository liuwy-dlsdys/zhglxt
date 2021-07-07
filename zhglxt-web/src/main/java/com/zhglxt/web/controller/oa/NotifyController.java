package com.zhglxt.web.controller.oa;

import com.zhglxt.common.annotation.Log;
import com.zhglxt.common.config.GlobalConfig;
import com.zhglxt.common.core.controller.BaseController;
import com.zhglxt.common.core.entity.AjaxResult;
import com.zhglxt.common.core.entity.Ztree;
import com.zhglxt.common.core.page.TableDataInfo;
import com.zhglxt.common.enums.BusinessType;
import com.zhglxt.common.util.ShiroUtils;
import com.zhglxt.oa.entity.Notify;
import com.zhglxt.oa.service.INotifyService;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @Description 通知通告Controller
 * @Author liuwy
 * @Date 2020/12/19
 */
@Controller
@RequestMapping("/oa/notify")
public class NotifyController extends BaseController {
    private String prefix = "oa/notify";

    @Autowired
    private INotifyService notifyService;

    @RequiresPermissions("oa:notify:view")
    @GetMapping()
    public String notice() {
        return prefix + "/notify";
    }

    @RequiresPermissions("oa:notify:myNotify")
    @GetMapping("/myNotify")
    public String myNotify() {
        return prefix + "/myNotify";
    }

    /**
     * 查询我的通告列表
     */
    @RequiresPermissions("oa:notify:myNotify")
    @PostMapping("/self")
    @ResponseBody
    public TableDataInfo self(Notify notify) {
        startPage();
        notify.setSelf(true);
        notify.setUserId(ShiroUtils.getUserId());
        List<Notify> list = notifyService.selectNotifyList(notify);
        return getDataTable(list);
    }

    /**
     * 查询通知管理列表
     */
    @RequiresPermissions("oa:notify:view")
    @PostMapping("/list")
    @ResponseBody
    public TableDataInfo list(Notify notify) {
        startPage();
        List<Notify> list = notifyService.selectNotifyList(notify);
        return getDataTable(list);
    }

    /**
     * 新增公告
     */
    @GetMapping("/add")
    public String add(ModelMap mmap) {
        return prefix + "/notifyAdd";
    }

    /**
     * 新增保存公告
     */
    @RequiresPermissions("oa:notify:add")
    @Log(title = "通知公告", businessType = BusinessType.INSERT)
    @PostMapping("/add")
    @ResponseBody
    public AjaxResult addSave(Notify notify) {
        if (GlobalConfig.isDemoEnabled()) {
            return error("演示模式不允许本操作");
        }
        return toAjax(notifyService.insertNotify(notify));
    }

    /**
     * 查看-我的通告
     */
    @GetMapping("/self/{notifyId}")
    public String selfView(@PathVariable("notifyId") String notifyId, ModelMap mmap) {
        Notify notify = new Notify();
        notify.setId(notifyId);
        Notify notifyEntity = notifyService.selectNotifyById(notify);
        mmap.put("notify", notifyEntity);

        //变更查阅状态为：已读
        notifyService.updateNotifyRecordByNotifyIdAndUserId(notifyEntity.getId(), ShiroUtils.getUserId());

        return prefix + "/notifyView";
    }

    /**
     * 修改公告
     */
    @GetMapping("/edit/{notifyId}")
    public String edit(@PathVariable("notifyId") String notifyId, ModelMap mmap) {
        Notify notify = new Notify();
        notify.setId(notifyId);
        Notify notifyEntity = notifyService.selectNotifyById(notify);
        notifyEntity.setDeptUserIds(notifyEntity.getUserIds());
        mmap.put("notify", notifyEntity);
        return prefix + "/notifyEdit";
    }

    /**
     * 修改保存公告
     */
    @RequiresPermissions("oa:notify:edit")
    @Log(title = "通知公告", businessType = BusinessType.UPDATE)
    @PostMapping("/edit")
    @ResponseBody
    public AjaxResult editSave(Notify notify) {
        if (GlobalConfig.isDemoEnabled()) {
            return error("演示模式不允许本操作");
        }
        notify.setUpdateBy(ShiroUtils.getLoginName());
        return toAjax(notifyService.updateNotify(notify));
    }

    /**
     * 删除公告
     */
    @RequiresPermissions("oa:notify:remove")
    @Log(title = "通知公告", businessType = BusinessType.DELETE)
    @PostMapping("/remove")
    @ResponseBody
    public AjaxResult remove(String ids) {
        if (GlobalConfig.isDemoEnabled()) {
            return error("演示模式不允许本操作");
        }
        return toAjax(notifyService.deleteNotifyByIds(ids));
    }

    /**
     * 加载部门人员列表树
     */
    @GetMapping("/deptUserTreeData")
    @ResponseBody
    public List<Ztree> deptUserTreeData(Notify notify) {
        List<Ztree> ztrees = notifyService.deptUserData(notify);
        return ztrees;
    }

}
