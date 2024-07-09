package com.metric.auth.server.controller.vo;

import lombok.Data;

import javax.validation.constraints.NotNull;

/**
 * @description:
 * @author: YangShu
 * @create: 2024-07-03
 **/
@Data
public class SysUserLoginParam {
    @NotNull
    private String key;
    @NotNull
    private String keyType;
    @NotNull
    private String password;
    @NotNull
    private Boolean rememberMe = false;
}
