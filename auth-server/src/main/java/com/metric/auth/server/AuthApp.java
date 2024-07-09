package com.metric.auth.server;

import com.metric.auth.core.util.SpringApplicationHelper;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.context.annotation.ComponentScan;

/**
 * @description:
 * @author: YangShu
 * @create: 2024-06-29
 **/
@SpringBootApplication
@MapperScan("com.metric.auth.core.persistence")
@ComponentScan("com.metric.auth")
public class AuthApp {

    public static void main(String[] args) {
        String profile = SpringApplicationHelper.getRuntiemProfile();
        new SpringApplicationBuilder(AuthApp.class)
                .profiles(profile)
                .run(args);
    }
}
