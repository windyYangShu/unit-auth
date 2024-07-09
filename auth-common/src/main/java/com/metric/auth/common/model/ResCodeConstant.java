package com.metric.auth.common.model;

/**
 * @description: 返回状态常量
 * @author: YangShu
 * @create: 2024-06-25
 **/
public final class ResCodeConstant {
    public static final int SUCCESS = 200;

    // 基础异常
    public static final int BAD_REQUEST = 4000;
    public static final int TOKEN_INVALID = 4001;
    public static final int UN_PROCESSABLE_REQUEST = 4002;
    public static final int NO_PERMISSION = 4003;
    public static final int RES_NOT_FOUNT = 4004;
    public static final int ENUM_NOT_PRESENT = 4005;
    public static final int RES_DUPLICATE_RECORD = 4009;

    public static final int SERVER_ERROR = 5000;
    public static final int REMOTE_SERVER_EXCEPTION = 5001;
    public static final int NETWORK_TIMEOUT = 5002;
    public static final int NETWORK_EXCEPTION = 5003;

    // 业务异常
    public static final int ROLE_CONFIG_ILLEGAL = 5100;
}
