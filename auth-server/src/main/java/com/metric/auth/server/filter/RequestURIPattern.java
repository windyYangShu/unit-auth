package com.metric.auth.server.filter;

import org.springframework.util.AntPathMatcher;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.List;

/**
 * @description: url 匹配
 *   优先excludePattern 然后 includePattern
 * @author: YangShu
 * @create: 2024-06-29
 **/
public class RequestURIPattern {

    private AntPathMatcher matcher = new AntPathMatcher();

    private List<String> includePattern = new ArrayList<>();

    private List<String> excludePattern = new ArrayList<>();

    public RequestURIPattern() {
    }

    public RequestURIPattern(String includePattern, String excludePattern) {
        this();
        this.includePattern.add(includePattern);
        this.excludePattern.add(excludePattern);
    }

    public RequestURIPattern addIncludePattern(String includePattern) {
        this.includePattern.add(includePattern);
        return this;
    }

    public RequestURIPattern addExcludePattern(String excludePattern) {
        this.excludePattern.add(excludePattern);
        return this;
    }

    /**
     * 判断请求URL是否需要校验
     * @param request
     * @return
     */
    public boolean isMatch(HttpServletRequest request) {
        // exclude包含的URL 不匹配
        if (!excludePattern.isEmpty()
                && excludePattern.stream()
                    .anyMatch(exclude -> matcher.match(exclude, request.getRequestURI()))) {
            return Boolean.FALSE;
        }

        // include不包含的URL 不匹配
        if (!includePattern.isEmpty()
                && includePattern.stream()
                    .noneMatch(include -> matcher.match(include, request.getRequestURI()))) {
            return Boolean.FALSE;
        }

        return Boolean.TRUE;
    }
}
