package com.metric.auth.server.config;

import com.github.xiaoymin.knife4j.spring.extension.OpenApiExtensionResolver;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.Contact;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

/**
 * @description: Knife4j 配置类
 *   访问地址：http://项目实际地址/doc.html
 * @author: YangShu
 * @create: 2024-06-25
 **/
@Configuration
@EnableSwagger2
public class Knife4jConfig {

    private final OpenApiExtensionResolver openApiExtensionResolver;

    /**
     * Knife4j 扩展类，通过扩展给增强模式插件赋值，实现如自定义文档等高级功能
     * @param openApiExtensionResolver
     */
    @Autowired
    public Knife4jConfig(OpenApiExtensionResolver openApiExtensionResolver) {
        this.openApiExtensionResolver = openApiExtensionResolver;
    }

    /**
     * Knife4j 3.x版本进行了升级，文档类型调整为OAS_30
     * @return
     */
    @Bean
    public Docket adminApi() {
        Docket docket = new Docket(DocumentationType.OAS_30)
                .host("192.168.64.130:9091")
                .apiInfo(apiInfo())
                .groupName("SSO 1.X 版本")
                .select()
                //这里指定Controller扫描包路径
                .apis(RequestHandlerSelectors.basePackage("com.metric.auth.server.controller"))
                .paths(PathSelectors.any())
                .build()
                .extensions(openApiExtensionResolver.buildExtensions("custom-doc-knife4j-1.1.0"));
        return docket;
    }

    private ApiInfo apiInfo() {
        return new ApiInfoBuilder()
                .title("SSO APIs")
                .description("# 一站式SSO, 提供网站登录、鉴权、转发相关功能")
                .termsOfServiceUrl("https://github.com/windyYangShu")
                .contact(new Contact("shuyang", "127.0.0.1", "windyangshu@gamail.com"))
                .version("1.1.0")
                .build();
    }

}
