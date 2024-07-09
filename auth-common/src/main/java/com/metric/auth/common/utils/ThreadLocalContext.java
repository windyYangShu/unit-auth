package com.metric.auth.common.utils;

import com.metric.auth.common.model.Subject;
import org.apache.commons.lang3.StringUtils;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

/**
 * @description: 全文上下文工具
 * @author: YangShu
 * @create: 2024-06-29
 **/
public class ThreadLocalContext {

    public static final String AUTH_SUBJECT = "AUTH_SUBJECT";
    public static final String REQUEST_ID_KEY = "Auth_ReqId";
    public static final String AUTHORIZATION = "Authorization";

    private static ThreadLocal<Map<String, Object>> threadLocal = ThreadLocal.withInitial(HashMap::new);

    /**
     * 获取请求TraceId
     * @param request
     * @return
     */
    public static String getOrCreateCurTraceId(HttpServletRequest request) {
        Object traceId = threadLocal.get().get(REQUEST_ID_KEY);

        if (traceId != null) {
            return traceId.toString().split(",")[0];
        }

        if (request == null) {
            threadLocal.get().put(REQUEST_ID_KEY, UUID.randomUUID().toString());
        } else {
            if (StringUtils.isBlank(request.getHeader(REQUEST_ID_KEY))) {
                threadLocal.get().put(REQUEST_ID_KEY, UUID.randomUUID().toString());
            } else {
                threadLocal.get().put(REQUEST_ID_KEY, request.getHeader(REQUEST_ID_KEY));
            }
        }

        return threadLocal.get().get(REQUEST_ID_KEY).toString().split(",")[0];
    }

    public static Optional<Object> get(String key) {
        return Optional.ofNullable(threadLocal.get().get(key));
    }

    public static void put(String key, Object value) {
        threadLocal.get().put(key, value);
    }

    public static Optional<Object> remove(String key) {
        return Optional.ofNullable(threadLocal.get().remove(key));
    }

    public static String getUserName() {
        Optional<Object> subOpt = get(AUTH_SUBJECT);
        if (subOpt.isPresent() && (subOpt.get() instanceof Subject)) {
            return ((Subject) subOpt.get()).getId();
        } else {
            return StringUtils.EMPTY;
        }
    }

    public static void clear() {
        threadLocal.get().clear();
    }

}
