package com.metric.auth.common.exceptions;


import com.metric.auth.common.model.ResponseCode;

/**
 * @description:
 * @author: YangShu
 * @create: 2024-06-26
 **/
public class RoleConfigIllegalException extends GlobalException {

    public RoleConfigIllegalException(String message) {
        super(message);
    }

    @Override
    public ResponseCode getResCode() {
        return ResponseCode.BAD_REQUEST;
    }
}
