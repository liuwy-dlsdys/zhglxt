package com.zhglxt;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.core.env.Environment;


/**
 * 启动程序
 *
 * @author ruoyi
 */
@SpringBootApplication(exclude = {DataSourceAutoConfiguration.class, SecurityAutoConfiguration.class})
public class ZhglxtApplication extends SpringBootServletInitializer {
    public static void main(String[] args) {
        ConfigurableApplicationContext run = SpringApplication.run(ZhglxtApplication.class, args);
        System.out.println("系统启动成功。。。");
        Environment env = run.getEnvironment();
        System.out.println("系统访问地址:http://localhost:" + env.getProperty("server.port") + env.getProperty("server.servlet.context-path"));
        System.out.println("CMS官网访问地址:http://localhost:" + env.getProperty("server.port") + env.getProperty("server.servlet.context-path")+"/cms/index.html");
        System.out.println("系统接口文档（swagger3）:http://localhost:" + env.getProperty("server.port") + env.getProperty("server.servlet.context-path") + "/swagger-ui/index.html（登录后访问）");
        System.out.println("系统接口文档（swagger3+knife4j）:http://localhost:" + env.getProperty("server.port") + env.getProperty("server.servlet.context-path") + "/doc.html（登录后访问）");
    }

    /**
     * web容器中进行部署
     */
    @Override
    protected SpringApplicationBuilder configure(SpringApplicationBuilder builder) {
        this.setRegisterErrorPageFilter(false);
        return builder.sources(ZhglxtApplication.class);
    }
}