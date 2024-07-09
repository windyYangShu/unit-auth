package com.metric.auth.common.model;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * @description:
 * @author: YangShu
 * @create: 2024-06-25
 **/
@ApiModel("请求结果")
@NoArgsConstructor
public class CommonRes<T> {

    @ApiModelProperty("请求结果状态码")
    @Getter
    private ResponseCode code;

    @ApiModelProperty("消息")
    @Getter
    private String message;

    @ApiModelProperty("数据对象")
    private T data;

    public static CommonRes<String> fail(String msg) {
        CommonRes<String> res = new CommonRes<>();
        res.setMessage(msg);
        return res;
    }

    public static <E> CommonRes<E> success(E data) {
        CommonRes<E> res = new CommonRes<>();
        res.setData(data);
        res.setCode(ResponseCode.SUCCESS);
        return res;
    }

    public static CommonRes<Void> success() {
        return new CommonRes<Void>().setCode(ResponseCode.SUCCESS);
    }

    public T getData() {
        return data;
    }

    public CommonRes<T> setCode(ResponseCode code) {
        this.code = code;
        return this;
    }

    public CommonRes<T> setMessage(String message) {
        this.message = message;
        return this;
    }

    public CommonRes<T> setData(T data) {
        this.data = data;
        return this;
    }
}
