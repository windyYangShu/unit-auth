package com.metric.auth.server.filter.filters;

import com.metric.auth.common.log.Logger;
import com.metric.auth.common.utils.InetAddressUtils;
import com.metric.auth.common.utils.ThreadLocalContext;
import com.metric.auth.server.filter.RequestURIPattern;
import org.springframework.core.Ordered;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * @description: api统计信息
 * @author: YangShu
 * @create: 2024-06-30
 **/
public class ApiStatisticFilter extends OncePerRequestFilter implements Ordered {

    private Logger logger = Logger.getLogger(ApiStatisticFilter.class);
    private RequestURIPattern pattern = new RequestURIPattern();
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
        if (pattern.isMatch(request)) {
            long time = 0L;
            try {
                long start = System.currentTimeMillis();
                filterChain.doFilter(request, response);
                time = System.currentTimeMillis() - start;
            } finally {
                logApiInfo(request, response, time);
            }
        } else {
            filterChain.doFilter(request, response);
        }
    }

    private void logApiInfo(HttpServletRequest request, HttpServletResponse response, long time) {
        try {
            logger.info("{} {} {} [{}] [{}ms] [{}]",
                    InetAddressUtils.getIpAddr(request),request.getMethod(), request.getRequestURI(),
                    response.getStatus(), time, ThreadLocalContext.getUserName());
        } catch (Exception e) {
            logger.error("ApiStatisticFilter error:", e);
        }
    }

    public ApiStatisticFilter setOrder(int order) {
        this.order = order;
        return this;
    }

    public ApiStatisticFilter setPattern(RequestURIPattern pattern) {
        this.pattern = pattern;
        return this;
    }
}
