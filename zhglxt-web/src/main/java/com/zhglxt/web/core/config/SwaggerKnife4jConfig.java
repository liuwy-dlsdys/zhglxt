package com.zhglxt.web.core.config;


import com.github.xiaoymin.knife4j.spring.extension.OpenApiExtensionResolver;
import com.zhglxt.common.config.GlobalConfig;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.Contact;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;

import java.util.Arrays;
import java.util.HashSet;

/**
 * @Description swagger接口配置
 *
 * @Author liuwy
 * @Date 2020/9/27
 */
@Configuration
public class SwaggerKnife4jConfig {

    /**
     * 是否开启swagger
     */
    @Value("${swagger.enabled}")
    private boolean enabled;

    private final OpenApiExtensionResolver openApiExtensionResolver;

    @Autowired
    public SwaggerKnife4jConfig(OpenApiExtensionResolver openApiExtensionResolver) {
        this.openApiExtensionResolver = openApiExtensionResolver;
    }

    @Bean(value = "adminApi")
    public Docket createRestApi() {
        return new Docket(DocumentationType.OAS_30)
                // 是否启用Swagger
                .enable(enabled)
                // 分组名称
                .groupName(GlobalConfig.getName())
                // 展示在文档首页中的基本信息
                .apiInfo(apiInfo())
                // 设置哪些接口暴露给Swagger展示
                .select()
                // 扫描所有有注解的api，用这种方式更灵活
                .apis(RequestHandlerSelectors.withMethodAnnotation(ApiOperation.class))
                // 扫描所有 .apis(RequestHandlerSelectors.any())
                .paths(PathSelectors.any())
                .build()
                // 扩展
                .extensions(openApiExtensionResolver.buildExtensions(GlobalConfig.getName()))
                // 支持的通讯协议集合
                .protocols(new HashSet<>(Arrays.asList("http","https")));
    }

    private ApiInfo apiInfo() {
        return new ApiInfoBuilder()
                //标题
                .title(GlobalConfig.getName()+"-接口文档")
                //描述
                .description(GlobalConfig.getName()+"-接口文档。如果不明白如何使用，请查看【自定义文档】中的【使用教程】")
                //作者
                .contact(new Contact(GlobalConfig.getName(), null, null))
                //服务url
                .termsOfServiceUrl("http://www.zhglxt.dlsdys.com")
                //版本号
                .version("版本号:" + GlobalConfig.getVersion())
                .build();
    }

}
