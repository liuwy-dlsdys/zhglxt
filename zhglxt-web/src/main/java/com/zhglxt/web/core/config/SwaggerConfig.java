package com.zhglxt.web.core.config;

import com.zhglxt.common.config.GlobalConfig;
import io.swagger.annotations.ApiOperation;
import io.swagger.models.auth.In;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.oas.annotations.EnableOpenApi;
import springfox.documentation.service.*;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spi.service.contexts.SecurityContext;
import springfox.documentation.spring.web.plugins.Docket;

import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;

/**
 * Swagger 接口配置
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
 * @author ruoyi
 */
@Configuration
@EnableOpenApi //swagger3用EnableOpenApi注解 ； swagger2用EnableSwagger2注解
public class SwaggerConfig {
    /**
     * 是否开启swagger
     */
    @Value("${swagger.enabled}")
    private boolean enabled;

    /**
     * 创建API
     */
    @Bean
    public Docket createRestApi() {
        return new Docket(DocumentationType.OAS_30)
                // 是否启用Swagger
                .enable(enabled)
                // 用来创建该API的基本信息，展示在文档的页面中（自定义展示的信息）
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
                .securitySchemes(securitySchemes())
                .securityContexts(securityContexts())
                // 支持的通讯协议集合
                .protocols(new HashSet<>(Arrays.asList("http","https")));
    }

    /**
     * 添加摘要信息
     */
    private ApiInfo apiInfo() {
        // 用ApiInfoBuilder进行定制
        return new ApiInfoBuilder()
                // 设置标题
                .title("标题："+GlobalConfig.getName()+"_接口文档")
                // 描述
                .description("描述：Swagger是一款让你更好的书写API文档的规范且完整框架；提供描述、生产、消费和可视化RESTful Web Service；" +
                        "庞大工具集合支撑的形式化规范。这个集合涵盖了从终端用户接口、底层代码库到商业API管理的方方面面。")
                // 作者信息
                .contact(new Contact(GlobalConfig.getName(), null, null))
                // 版本
                .version("版本号:" + GlobalConfig.getVersion())
                .build();
    }

    /**
     * @Description 设置授权信息
     * @Author liuwy
     * @Date 2021/6/9
     */
    private List<SecurityScheme> securitySchemes(){
        ApiKey apiKey=new ApiKey("BASE_TOKEN","token", In.HEADER.toValue());
        return Collections.singletonList(apiKey);
    }

    /**
     * @Description 授权信息全局应用
     * @Author liuwy
     * @Date 2021/6/9
     */
    private List<SecurityContext> securityContexts(){
        return Collections.singletonList(
                SecurityContext
                        .builder()
                        .securityReferences(
                                Collections.singletonList(
                                        new SecurityReference("BASE_TOKEN",
                                                new AuthorizationScope[]{
                                                        new AuthorizationScope("global","")
                                                })
                                )
                        ).build()
        );

    }
}
