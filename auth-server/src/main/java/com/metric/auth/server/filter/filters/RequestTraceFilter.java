package com.metric.auth.server.filter.filters;

import com.metric.auth.common.utils.ThreadLocalContext;
import com.metric.auth.server.filter.RequestURIPattern;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.Ordered;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * @description: 生成traceId
 * @author: YangShu
 * @create: 2024-06-29
 **/
public class RequestTraceFilter extends OncePerRequestFilter implements Ordered {

    private static final Logger LOGGER_WITHOUT_TRACE = LoggerFactory.getLogger(RequestTraceFilter.class);
    private int order = 0;
    private RequestURIPattern pattern = new RequestURIPattern();

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
            try {
                if (pattern.isMatch(request)) {
                    ThreadLocalContext.getOrCreateCurTraceId(request);
                }
            } catch (Exception e) {
                LOGGER_WITHOUT_TRACE.error("create TraceId error: " + e.getLocalizedMessage(), e);
            }
            filterChain.doFilter(request, response);
        } finally {
            try {
                ThreadLocalContext.clear();
            } catch (Exception e) {
                LOGGER_WITHOUT_TRACE.error("clear" + Thread.currentThread().toString()
                        + " ThreadLocalContext error: " + e.getLocalizedMessage(), e);
            }
        }

    }

    public RequestTraceFilter setOrder(int orderValue) {
        this.order = orderValue;
        return this;
    }

    public RequestTraceFilter setPattern(RequestURIPattern pattern) {
        this.pattern = pattern;
        return this;
    }
}
