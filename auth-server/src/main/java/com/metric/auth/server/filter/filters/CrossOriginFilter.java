package com.metric.auth.server.filter.filters;

import com.metric.auth.server.filter.RequestURIPattern;
import org.apache.commons.lang3.StringUtils;
import org.springframework.core.Ordered;
import org.springframework.http.HttpStatus;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * @description: 跨域请求处理
 * @author: YangShu
 * @create: 2024-06-29
 **/
public class CrossOriginFilter extends OncePerRequestFilter implements Ordered {

    private RequestURIPattern pattern = new RequestURIPattern();
    private int order = 0;

    @Override
    public int getOrder() {
        return order;
    }

    public CrossOriginFilter setOrder(int order) {
        this.order = order;
        return this;
    }

    public CrossOriginFilter setPattern(RequestURIPattern pattern) {
        this.pattern = pattern;
        return this;
    }


    @Override
    protected void doFilterInternal(
            HttpServletRequest request,
            HttpServletResponse response,
            FilterChain filterChain) throws ServletException, IOException {
        if (pattern.isMatch(request)) {
            String curOrigin = request.getHeader("Origin");
            // Access-Control-Allow-Origin 指定授权访问的域
            if (!StringUtils.isEmpty(curOrigin)) {
                response.addHeader("Access-Control-Allow-Origin", curOrigin);
            }
            response.addHeader("Access-Control-Allow-Methods",
                    "POST, GET, OPTIONS, DELETE, PATCH, PUT");
            response.addHeader("Access-Control-Allow-Headers",
                    "X-Requested-With, Content-Type, Accept, Authorization,"
                            + " access-control-allow-origin");
            response.addHeader("Access-Control-Allow-Credentials", "true");

            //设置跨域访问预检查操作响应200
            if (request.getMethod().equalsIgnoreCase("options")) {
                response.setStatus(HttpStatus.OK.value());
            } else {
                filterChain.doFilter(request, response);
            }
        } else {
            filterChain.doFilter(request, response);
        }
    }
}
