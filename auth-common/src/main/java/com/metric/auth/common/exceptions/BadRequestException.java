package com.metric.auth.common.exceptions;


import com.metric.auth.common.model.ResponseCode;

/**
 * @description:
 * @author: YangShu
 * @create: 2024-06-25
 **/
public class BadRequestException extends GlobalException {

    public BadRequestException(String message) {
        super(message);
    }

    @Override
    public ResponseCode getResCode() {
        return ResponseCode.BAD_REQUEST;
    }

}
