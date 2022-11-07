package com.zhglxt.quartz.config;

import org.quartz.Scheduler;
import org.quartz.SchedulerException;
import org.quartz.impl.StdSchedulerFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.annotation.WebListener;

/**
 * @Description 解决ShutDown时，没有关闭 Scheduler_Worker-x 线程问题。否则可能会造成内存泄漏
 * @Author liuwy
 * @Date 2022/11/7
 **/
@WebListener
public class ShutDownQuartzListener implements ServletContextListener {
    private Logger logger = LoggerFactory.getLogger(ShutDownQuartzListener.class);
    @Override
    public void contextInitialized(ServletContextEvent sce) {
        ServletContextListener.super.contextInitialized(sce);
    }

    @Override
    public void contextDestroyed(ServletContextEvent sce) {
        try {
            Scheduler defaultScheduler = StdSchedulerFactory.getDefaultScheduler();
            defaultScheduler.shutdown(true);

        } catch (SchedulerException e) {
            logger.error("SchedulerException",e);
        }
        ServletContextListener.super.contextDestroyed(sce);
    }
}
