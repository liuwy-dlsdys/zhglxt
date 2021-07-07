package com.zhglxt.activiti.controller;

import com.zhglxt.activiti.util.ActUtils;
import com.zhglxt.common.core.entity.sys.SysUser;
import com.zhglxt.system.service.impl.SysRoleServiceImpl;
import com.zhglxt.system.service.impl.SysUserServiceImpl;
import org.activiti.engine.identity.Group;
import org.activiti.engine.impl.GroupQueryImpl;
import org.activiti.engine.impl.Page;
import org.activiti.engine.impl.persistence.entity.GroupEntityManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * 自定义组实体管理类
 * @author liuwy
 * @date 2019/11/4
 */
@Component
public class CustomGroupEntityManager extends GroupEntityManager {
    @Lazy
    @Autowired
    private SysRoleServiceImpl roleService;

    @Lazy
    @Autowired
    private SysUserServiceImpl userService;

    @Override
    public List<Group> findGroupsByUser(String loginName) {
        if (loginName == null) {
            return null;
        }
        try {
            SysUser user = userService.selectUserByLoginName(loginName);
            Set<String> roleKeys = roleService.selectRoleKeys(user.getUserId());
            List<Group> gs = ActUtils.toActivitiGroups(roleKeys);
            return gs;
        } catch (EmptyResultDataAccessException e) {
            return null;
        }
    }

    @Override
    public List<Group> findGroupByQueryCriteria(GroupQueryImpl query, Page page) {
        throw new RuntimeException("not implement method.");
    }

    @Override
    public long findGroupCountByQueryCriteria(GroupQueryImpl query) {
        throw new RuntimeException("not implement method.");
    }

    @Override
    public List<Group> findGroupsByNativeQuery(Map<String, Object> parameterMap, int firstResult, int maxResults) {
        throw new RuntimeException("not implement method.");
    }

    @Override
    public long findGroupCountByNativeQuery(Map<String, Object> parameterMap) {
        throw new RuntimeException("not implement method.");
    }
}
