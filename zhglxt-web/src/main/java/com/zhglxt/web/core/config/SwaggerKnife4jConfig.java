package com.zhglxt.web.core.config;


import com.github.xiaoymin.knife4j.spring.annotations.EnableKnife4j;
import com.zhglxt.common.config.GlobalConfig;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.oas.annotations.EnableOpenApi;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.Contact;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;

import java.util.Arrays;
import java.util.HashSet;

/**
 * @Description swagger Knife4j
 *
 * 一些常用注解说明
 * @Api：用在controller类，描述API接口
 * @ApiOperation：描述接口方法
 * @ApiModel：描述对象
 * @ApiModelProperty：描述对象属性
 * @ApiImplicitParams：描述接口参数
 * @ApiResponses：描述接口响应
 * @ApiIgnore：忽略接口方法
 *
 * @Author liuwy
 * @Date 2020/9/27
 */
@Configuration
@EnableOpenApi //swagger3用EnableOpenApi注解 ； swagger2用EnableSwagger2注解
@EnableKnife4j //该注解是knife4j提供的增强注解,Ui提供了例如动态参数、参数过滤、接口排序等增强功能,如果你想使用这些增强功能就必须加该注解，否则可以不用加
public class SwaggerKnife4jConfig {

    /**
     * 是否开启swagger
     */
    @Value("${swagger.enabled}")
    private boolean enabled;

    @Bean(value = "adminApi")
    public Docket createRestApi() {
        return new Docket(DocumentationType.OAS_30)
                // 是否启用Swagger
                .enable(enabled)
                // 用来创建该API的基本信息，展示在文档的页面中（自定义展示的信息）
                //分组名称
                .groupName(GlobalConfig.getName())
                .apiInfo(apiInfo())
                // 设置哪些接口暴露给Swagger展示
                .select()
                // 扫描所有有注解的api，用这种方式更灵活
                .apis(RequestHandlerSelectors.withMethodAnnotation(ApiOperation.class))
                // 扫描指定包中的swagger注解
                //.apis(RequestHandlerSelectors.basePackage("com.zhglxt.project.tool.swagger"))
                // 扫描所有 .apis(RequestHandlerSelectors.any())
                .paths(PathSelectors.any())
                .build()
                // 支持的通讯协议集合
                .protocols(new HashSet<>(Arrays.asList("http","https")));
    }

    private ApiInfo apiInfo() {
        return new ApiInfoBuilder()
                //标题
                .title(GlobalConfig.getName()+"-接口文档")
                //描述
                .description(GlobalConfig.getName()+"-接口文档")
                //作者
                .contact(new Contact(GlobalConfig.getName(), null, null))
                //服务url
                .termsOfServiceUrl("http://www.zhglxt.dlsdys.com")
                //版本号
                .version("版本号:" + GlobalConfig.getVersion())
                .build();
    }

}
