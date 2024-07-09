package com.metric.auth.server.filter;

import com.metric.auth.server.filter.filters.ApiStatisticFilter;
import com.metric.auth.server.filter.filters.CrossOriginFilter;
import com.metric.auth.server.filter.filters.GlobalExceptionFilter;
import com.metric.auth.server.filter.filters.RequestTraceFilter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @description: filterChain配置 在springsecurity filter前执行
 * @author: YangShu
 * @create: 2024-06-29
 **/
@Configuration
public class FilterConfig {

    /**
     * 定义filter顺序
     */
    private static final int CROSS_ORIGIN_FILTER_ORDER = Integer.MIN_VALUE;
    private static final int REQUEST_TRACE_FILTER_ORDER = Integer.MIN_VALUE + 1;
    private static final int API_STATISTIC_FILTER_ORDER = Integer.MIN_VALUE + 2;
    private static final int GLOBAL_EXCEPTION_FILTER_ORDER = Integer.MIN_VALUE + 3;
    private static final int AUTH_FILTER_ORDER = Integer.MIN_VALUE + 4;

    @Bean
    public CrossOriginFilter crossOriginFilter() {
        return new CrossOriginFilter().setOrder(CROSS_ORIGIN_FILTER_ORDER);
    }

    @Bean
    public RequestTraceFilter requestTraceFilter() {
        return new RequestTraceFilter().setOrder(REQUEST_TRACE_FILTER_ORDER).setPattern(
                new RequestURIPattern()
                        .addExcludePattern("/**/*.*")
                        .addExcludePattern("/error")
                        .addExcludePattern("/auth/swagger/**")
                        .addExcludePattern("/exceptionHandler")
        );
    }

    @Bean
    public ApiStatisticFilter apiStatisticFilter() {
        return new ApiStatisticFilter().setOrder(API_STATISTIC_FILTER_ORDER).setPattern(
                new RequestURIPattern()
                        .addExcludePattern("/error")
                        .addExcludePattern("/auth/swagger/*")
                        .addExcludePattern("/exceptionHandler")
        );
    }

    @Bean
    public GlobalExceptionFilter globalExceptionFilter() {
        return new GlobalExceptionFilter().setOrder(GLOBAL_EXCEPTION_FILTER_ORDER);
    }

//    @Bean
//    public AuthFilter authFilter() {
//        RequestURIPattern pattern = new RequestURIPattern()
//                .addExcludePattern("/auth/error")
//                .addExcludePattern("/auth/exceptionHandler")
//                .addExcludePattern("/auth/v3/api-docs")
//                .addExcludePattern("/auth/auth-doc-json")
//                .addExcludePattern("/auth/doc.html")
//                .addExcludePattern("/auth/**")
//                .addExcludePattern("/doc.html")
//                .addExcludePattern("/auth/swagger/**")
//                .addExcludePattern("/auth/api/v3/resource/block-item/run")
//                .addExcludePattern("/auth/*.xlsx")
//                .addExcludePattern("/**/*.html")
//                .addExcludePattern("/auth/favicon.ico")
//                .addExcludePattern("/auth/favicon")
//                .addExcludePattern("/auth/index.html*")
//                .addExcludePattern("/auth/manifest.json*")
//                .addExcludePattern("/auth/robots.txt*")
//                .addExcludePattern("/auth/webjars/**")
//                .addExcludePattern("/auth/swagger-resources/**")
//                .addExcludePattern("/v1/api-docs-ext")
//                .addExcludePattern("/auth/precache-manifest.*")
//                .addExcludePattern("/auth/init/**");
//        return new AuthFilter().setOrder(AUTH_FILTER_ORDER).setPattern(pattern);
//    }
}
