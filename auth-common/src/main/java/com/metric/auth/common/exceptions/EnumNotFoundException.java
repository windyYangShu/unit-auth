package com.metric.auth.common.exceptions;

/**
 * @description:
 * @author: YangShu
 * @create: 2024-06-25
 **/
public class EnumNotFoundException extends GlobalException {

    public EnumNotFoundException(String message) {
        super(message);
    }

    public EnumNotFoundException(String message, Exception e) {
        super(message, e);
    }
}
