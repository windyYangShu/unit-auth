package com.metric.auth.common.exceptions;


import com.metric.auth.common.model.ResponseCode;
import org.apache.commons.lang3.StringUtils;

/**
 * @description: 全局异常处理
 * @author: YangShu
 * @create: 2024-06-25
 **/
public class GlobalException extends RuntimeException {

    public GlobalException(String message) {
        super(message);
    }

    public GlobalException(Throwable e) {
        super(e);
    }

    public GlobalException(String message, Throwable e) {
        super(
                (StringUtils.isNotBlank(message) ? message + ", cause: " : StringUtils.EMPTY)
                        + e.getLocalizedMessage(),
                e
        );
    }

    public ResponseCode getResCode() {
        return ResponseCode.SERVER_ERROR;
    }
}
