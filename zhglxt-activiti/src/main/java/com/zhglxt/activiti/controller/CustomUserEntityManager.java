package com.zhglxt.activiti.controller;

import com.zhglxt.activiti.util.ActUtils;
import com.zhglxt.common.core.entity.sys.SysUser;
import com.zhglxt.system.service.impl.SysRoleServiceImpl;
import com.zhglxt.system.service.impl.SysUserServiceImpl;
import org.activiti.engine.identity.Group;
import org.activiti.engine.impl.Page;
import org.activiti.engine.impl.UserQueryImpl;
import org.activiti.engine.impl.persistence.entity.IdentityInfoEntity;
import org.activiti.engine.impl.persistence.entity.UserEntity;
import org.activiti.engine.impl.persistence.entity.UserEntityManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * 自定义用户实体管理类
 * @author liuwy
 * @date 2019/11/4
 */
@Component
public class CustomUserEntityManager extends UserEntityManager {
    @Autowired
    @Lazy
    private SysUserServiceImpl userService;
    @Autowired
    @Lazy
    private SysRoleServiceImpl roleService;

    @Override
    public UserEntity findUserById(String userId) {
        SysUser user = getUser(userId);
        return ActUtils.toActivitiUser(user);
    }

    @Override
    public List<Group> findGroupsByUser(String loginName) {
        if (loginName == null) {
            return null;
        }
        SysUser user = userService.selectUserByLoginName(loginName);
        Set<String> roleKeys = roleService.selectRoleKeys(user.getUserId());
        List<Group> gs = ActUtils.toActivitiGroups(roleKeys);
        return gs;
    }

    @Override
    public List<org.activiti.engine.identity.User> findUserByQueryCriteria(UserQueryImpl query, Page page) {
        SysUser user = getUser(query.getId());
        List<org.activiti.engine.identity.User> list = new ArrayList<>();
        list.add(ActUtils.toActivitiUser(user));
        return list;
    }

    private SysUser getUser(String userId) {
        SysUser user = userService.selectUserById(userId);
        return user;
    }

    @Override
    public long findUserCountByQueryCriteria(UserQueryImpl query) {
        throw new RuntimeException("not implement method.");
    }

    @Override
    public IdentityInfoEntity findUserInfoByUserIdAndKey(String userId, String key) {
        throw new RuntimeException("not implement method.");
    }

    @Override
    public List<String> findUserInfoKeysByUserIdAndType(String userId, String type) {
        throw new RuntimeException("not implement method.");
    }

    @Override
    public List<org.activiti.engine.identity.User> findPotentialStarterUsers(String proceDefId) {
        throw new RuntimeException("not implement method.");
    }

    @Override
    public List<org.activiti.engine.identity.User> findUsersByNativeQuery(Map<String, Object> parameterMap,
                                                                          int firstResult, int maxResults) {
        throw new RuntimeException("not implement method.");
    }

    @Override
    public long findUserCountByNativeQuery(Map<String, Object> parameterMap) {
        throw new RuntimeException("not implement method.");
    }
}
