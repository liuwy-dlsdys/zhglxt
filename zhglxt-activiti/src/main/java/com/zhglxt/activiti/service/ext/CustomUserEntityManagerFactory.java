package com.zhglxt.activiti.service.ext;

import com.zhglxt.activiti.controller.CustomUserEntityManager;
import org.activiti.engine.impl.interceptor.Session;
import org.activiti.engine.impl.interceptor.SessionFactory;
import org.activiti.engine.impl.persistence.entity.UserIdentityManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * 自定义实现Activiti用户管理接口
 * @author liuwy
 * @date 2019/11/4
 */
@Service
public class CustomUserEntityManagerFactory implements SessionFactory {
    /*
        自定义类CustomUserEntityManagerFactory实现SessionFactory接口，重写getSessionType和openSession方法，核心代码如下：
    */
    @Autowired
    private CustomUserEntityManager customUserEntityManager;

    @Override
    public Class<?> getSessionType() {
        return UserIdentityManager.class;
    }

    @Override
    public Session openSession() {
        return customUserEntityManager;
    }
}
