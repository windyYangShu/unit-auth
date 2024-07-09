package com.metric.auth.server.controller.vo;

import lombok.Data;

/**
 * @description:
 * @author: YangShu
 * @create: 2024-07-02
 **/
@Data
public class SysUserReqParam {

    private Integer id;

    private String username;

    private String telephone;

    private String mail;

    private Integer deptId;

    private String password;

    private String rePassword;
}
