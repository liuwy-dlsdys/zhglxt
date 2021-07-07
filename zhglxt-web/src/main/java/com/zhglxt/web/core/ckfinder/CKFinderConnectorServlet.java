package com.zhglxt.web.core.ckfinder;

import com.zhglxt.common.util.file.FileUtils;
import com.zhglxt.file.manager.connector.ConnectorServlet;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.servlet.ServletRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;

/**
 * @author liuwy
 * @date 2019/11/29
 */
@Component
@Configuration
public class CKFinderConnectorServlet {

    @Value("${zhglxt.profile}")
    private String profile;//文件保存路径

    @Value("${zhglxt.ckfinderEnabled}")
    private String ckfinderEnabled;//是否启用CKFinder插件

    @Bean
    public ServletRegistrationBean connectCKFinder() {
        ServletRegistrationBean registrationBean = new ServletRegistrationBean(new ConnectorServlet(), "/ckfinder/core/connector/java/connector.java");
        registrationBean.addInitParameter("XMLConfig", "classpath:ckfinder.xml");
        registrationBean.addInitParameter("debug", "false");
        registrationBean.addInitParameter("configuration", "com.zhglxt.web.core.ckfinder.CKFinderConfig");

        /*
         * baseDir和baseURL都应该指向服务器上相同的位置  ——userfiles目录，其中包含CKFinder上传的所有用户文件.
         * baseDir是上传文件目录在服务器中的绝对路径.
         * baseURL是一个浏览器的访问路径(可以使用相对路径).
         *
         * baseDir跟baseURL其实都是指向同一个文件目录。为什么配置了baseURL之后一般情况下也需要配置baseDir参数？
         * 这是因为服务器配置原因导致有可能根据baseURL找不到路径而出现故障,这个时候baseDir就能派上用场了。
         * 因为它是一个绝对路径，所以不受服务器配置的影响。
         *
         *  */
        //初始化ckfinder.xml 配置
        registrationBean.addInitParameter("enabled", ckfinderEnabled);
        registrationBean.addInitParameter("baseDir", FileUtils.path(profile));
        registrationBean.addInitParameter("baseURL", FileUtils.path(profile));
        return registrationBean;
    }
}
