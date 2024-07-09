package com.metric.auth.core.util;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Optional;

/**
 * @description:
 * @author: YangShu
 * @create: 2024-07-07
 **/
public class SpringSecurityUtil {
    /**
     * 从上下文中获取当前登录的用户信息
     */
    public static Optional<String> getCurrentUserLogin() {
        // 获取上下文对象
        SecurityContext context = SecurityContextHolder.getContext();
        // 获取验证信息
        Authentication authentication = context.getAuthentication();
        // 返回上下文中的用户信息
        return Optional.ofNullable(authentication)
                .map(auth -> {
                    if (auth.getPrincipal() instanceof UserDetails) {
                        UserDetails userDetails = (UserDetails) auth.getPrincipal();

                        return userDetails.getUsername();
                    } else if (auth.getPrincipal() instanceof String) {
                        return (String) auth.getPrincipal();
                    }
                    return null;
                });

    }
}
