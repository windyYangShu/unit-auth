package com.metric.auth.server.controller.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.*;

/**
 * @description:
 * @author: YangShu
 * @create: 2024-07-02
 **/
//@PasswordValid
@Data
public class UserParam {

    private Integer id;

    @NotBlank(message = "用户名不可以为空")
    @Size(min = 1, max = 20, message = "用户名长度需要在20个字以内")
    private String username;

    @NotBlank(message = "电话不可以为空")
    @Size(min = 1, max = 13, message = "电话长度需要在13个字以内")
    private String telephone;

    @NotBlank(message = "邮箱不允许为空")
    @Email(message = "邮箱格式不符合")
    private String mail;

    @NotNull(message = "必须提供用户所在的部门")
    private Integer deptId;

    @ApiModelProperty("密码")
    @NotNull
    private String password;

    @ApiModelProperty("确认密码")
    @NotNull
    private String rePassword;

    @NotNull(message = "必须指定用户的状态")
    @Min(value = 0, message = "用户状态不合法")
    @Max(value = 2, message = "用户状态不合法")
    private Integer status;

    @Size(min = 0, max = 200, message = "备注长度需要在200个字以内")
    private String remark = "";


}
