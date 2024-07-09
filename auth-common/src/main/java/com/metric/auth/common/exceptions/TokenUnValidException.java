package com.metric.auth.common.exceptions;


import com.metric.auth.common.model.ResponseCode;

/**
 * @description: token无效异常
 * @author: YangShu
 * @create: 2024-06-30
 **/
public class TokenUnValidException extends GlobalException {

    public TokenUnValidException(String message) {
        super(message);
    }

    public TokenUnValidException(String message, Exception e) {
        super(message, e);
    }

    @Override
    public ResponseCode getResCode() {
        return ResponseCode.TOKEN_INVALID;
    }

}
