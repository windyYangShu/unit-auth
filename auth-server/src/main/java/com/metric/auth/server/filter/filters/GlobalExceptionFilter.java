package com.metric.auth.server.filter.filters;

import org.springframework.core.Ordered;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * @description: 定义全局filter异常，使filter的异常可以用自定义的异常信息包装给前端，防止后台直接响应web容器的error页面
 * @author: YangShu
 * @create: 2024-06-29
 **/
public class GlobalExceptionFilter extends OncePerRequestFilter implements Ordered {

    public static final String EXCEPTION_ATTRIBUTE =
            GlobalExceptionFilter.class.getSimpleName() + ".EXCEPTION";
    private int order = 0;


    @Override
    public int getOrder() {
        return order;
    }

    @Override
    protected void doFilterInternal(
            HttpServletRequest request,
            HttpServletResponse response,
            FilterChain filterChain) throws ServletException, IOException {
        try {
            filterChain.doFilter(request, response);
        } catch (Exception e) {
            // 记录异常对象
            request.setAttribute(EXCEPTION_ATTRIBUTE, e);
            request.getRequestDispatcher("/exceptionHandler").forward(request, response);
        } finally {
            request.removeAttribute(GlobalExceptionFilter.EXCEPTION_ATTRIBUTE);
        }
    }

    public GlobalExceptionFilter setOrder(int order) {
        this.order = order;
        return this;
    }
}
