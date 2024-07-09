package com.metric.auth.common.model;

import com.fasterxml.jackson.annotation.JsonValue;
import com.metric.auth.common.exceptions.GlobalException;
import lombok.Getter;

import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Stream;

/**
 * @description:
 * @author: YangShu
 * @create: 2024-06-25
 **/

public enum ResponseCode {
    SUCCESS(ResCodeConstant.SUCCESS, "成功"),
    BAD_REQUEST(ResCodeConstant.BAD_REQUEST, "参数格式不正确"),
    SERVER_ERROR(ResCodeConstant.SERVER_ERROR, "失败"),
    TOKEN_INVALID(ResCodeConstant.TOKEN_INVALID, "认证失败"),
    NETWORK_EXCEPTION(ResCodeConstant.NETWORK_EXCEPTION, "网络访问异常"),
    NETWORK_TIMEOUT(ResCodeConstant.NETWORK_TIMEOUT, "网络访问超时"),
    NO_PERMISSION(ResCodeConstant.NO_PERMISSION, "无权限"),
    RES_DUPLICATE_RECORD(ResCodeConstant.RES_DUPLICATE_RECORD, "配置资源重复"),
    RES_NOT_FOUND(ResCodeConstant.RES_NOT_FOUNT, "资源不存在"),
    ROLE_CONFIG_ILLEGAL(ResCodeConstant.ROLE_CONFIG_ILLEGAL, "用户角色配置非法");

    @Getter
    private int code;
    @Getter
    private String msg;

    ResponseCode(int code, String msg) {
        this.code = code;
        this.msg = msg;
    }

    public static Map<Integer, String> getResCodeAndMsg() {
        return Arrays.stream(ResponseCode.values())
                .collect(LinkedHashMap::new,
                        (h, v) -> h.put(v.getCode(), v.getMsg()),
                        LinkedHashMap::putAll);
    }

    public static ResponseCode of(Integer code) {
        Optional<ResponseCode> opt =
                Stream.of(ResponseCode.values()).filter(rc -> code.equals(rc.getCode())).findFirst();
        if (!opt.isPresent()) {
            throw new GlobalException("错误的状态码，code:" + code);
        }
        return opt.get();
    }

    @JsonValue
    public int getValue() {
        return this.code;
    }

}
