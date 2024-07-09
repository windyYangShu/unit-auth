package com.metric.auth.common.exceptions;

import com.metric.auth.common.model.ResponseCode;

import java.io.IOException;

/**
 * @description: 请求其他系统时网络异常
 * @author: YangShu
 * @create: 2024-06-25
 **/
public class NetworkException extends GlobalException {

    public NetworkException(String msg) {
        super(msg);
    }

    public NetworkException(IOException e) {
        super(e);
    }

    @Override
    public ResponseCode getResCode() {
        return ResponseCode.NETWORK_EXCEPTION;
    }

}
