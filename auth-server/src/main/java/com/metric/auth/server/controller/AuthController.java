package com.metric.auth.server.controller;

import com.metric.auth.common.log.Logger;
import com.metric.auth.common.model.CommonRes;
import com.metric.auth.common.model.SecurityConstants;
import com.metric.auth.common.utils.JacksonUtils;
import com.metric.auth.core.entites.SysJwtUser;
import com.metric.auth.server.controller.vo.SysUserLoginParam;
import com.metric.auth.server.service.SecurityService;
import com.metric.auth.server.service.SysUserService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;


/**
 * @description:
 * @author: YangShu
 * @create: 2024-07-09
 **/
@Api(tags = "权限管理")
@RestController
@RequestMapping("/api/v1")
public class AuthController {

    @Autowired
    private SecurityService securityService;
    @Autowired
    private SysUserService sysUserService;

    private static final Logger LOGGER = Logger.getLogger(AuthController.class);

    @ApiOperation("内容加密")
    @RequestMapping(value = "/encrypt", method = RequestMethod.GET)
    public CommonRes<String> encrypt(
            @RequestParam(defaultValue = "AES", required = false) String type,
            @RequestParam String rawText){
        LOGGER.info("encryptType:{}, rawText:{}", type, rawText);
        return CommonRes.success(securityService.encrypt(type, rawText));
    }

    @ApiOperation("用户登录")
    @RequestMapping(value = "/user/login", method = RequestMethod.POST)
    public CommonRes<String> login(@RequestBody SysUserLoginParam userLoginParam){
        LOGGER.info("login, userLoginDTO:{}", JacksonUtils.toJson(userLoginParam));
        // 用户登录认证
        SysJwtUser jwtUser = sysUserService.authLogin(userLoginParam);
        // todo 必须要实现响应头存放token，不然无法实现token的自动续签， 在filter中实现，响应带上authorization
        return CommonRes.success(SecurityConstants.TOKEN_PREFIX + jwtUser.getToken());
    }

    @ApiOperation("用户注销")
    @RequestMapping(value = "/user/logout", method = RequestMethod.POST)
    public CommonRes<Void> logout(){
//        LOGGER.info("login, userLoginDTO:{}", JacksonUtil.toJson(userLoginDTO));
        // 用户登录认证
        sysUserService.authLogout();
        return CommonRes.success();
    }
}
