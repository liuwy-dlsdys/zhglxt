package com.zhglxt.activiti.util;

import com.zhglxt.common.core.entity.sys.SysUser;
import com.zhglxt.common.util.SpringContextHolder;
import com.zhglxt.system.mapper.SysRoleMapper;
import com.zhglxt.system.mapper.SysUserMapper;

/**
 * 用户工具类
 *
 * @author liuwy
 * @version 2019/10/12
 */
public class UserUtils {

    private static SysUserMapper userDao = SpringContextHolder.getBean(SysUserMapper.class);
    private static SysRoleMapper roleDao = SpringContextHolder.getBean(SysRoleMapper.class);

    /**
     * 根据登录名获取用户
     *
     * @param loginName
     * @return 取不到返回null
     */
    public static SysUser getByLoginName(String loginName) {
        SysUser user = userDao.selectUserByLoginName(loginName);
        if (user != null) {
            user.setRoles(roleDao.selectRolesByUserId(user.getRoleId()));
        }
        return user;
    }

    public static SysUser getById(String startUserId) {
        SysUser user = userDao.selectUserById(startUserId);
        if (user != null) {
            user.setRoles(roleDao.selectRolesByUserId(user.getRoleId()));
        }
        return user;
    }
}
