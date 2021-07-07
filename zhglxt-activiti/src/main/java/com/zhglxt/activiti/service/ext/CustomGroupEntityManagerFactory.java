package com.zhglxt.activiti.service.ext;

import com.zhglxt.activiti.controller.CustomGroupEntityManager;
import org.activiti.engine.impl.interceptor.Session;
import org.activiti.engine.impl.interceptor.SessionFactory;
import org.activiti.engine.impl.persistence.entity.GroupIdentityManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * 自定义用户组实体管理
 * @author liuwy
 * @date 2019/11/4
 */
@Service
public class CustomGroupEntityManagerFactory implements SessionFactory {
    @Autowired
    private CustomGroupEntityManager customGroupEntityManager;

    @Override
    public Class<?> getSessionType() {
        return GroupIdentityManager.class;
    }

    @Override
    public Session openSession() {
        return customGroupEntityManager;
    }
}
