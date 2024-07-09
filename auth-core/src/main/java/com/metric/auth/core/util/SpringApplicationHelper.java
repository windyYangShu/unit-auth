package com.metric.auth.core.util;

import com.metric.auth.common.log.Logger;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeansException;
import org.springframework.boot.context.config.ConfigFileApplicationListener;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;

/**
 * @description:
 * @author: YangShu
 * @create: 2024-06-29
 **/
@Component
public class SpringApplicationHelper implements ApplicationContextAware {

    private static final String DOCKER_ENV_VAR = "DOCKER_ENV";
    private static final String DEFAULT_PROFILE = "local";

    private static final Logger LOGGER = Logger.getLogger(SpringApplicationHelper.class);

    private static ApplicationContext context;

    public static ApplicationContext getContext() {
        return context;
    }

    public static String getRuntiemProfile() {
        String profile =
                System.getenv(DOCKER_ENV_VAR) == null ? DEFAULT_PROFILE : System.getenv(DOCKER_ENV_VAR);

        String jvmProfile =
                System.getProperty(ConfigFileApplicationListener.ACTIVE_PROFILES_PROPERTY);

        if (StringUtils.isNotBlank(jvmProfile)) {
            profile= jvmProfile;
        }

        LOGGER.info("runtime profile={}", profile);
        return profile;
    }

    public static <T> T getBean(String name) {
        return (T) getContext().getBean(name);
    }

    public static <T> T getBean(Class<T> clazz) {
        return getContext().getBean(clazz);
    }

    public static <T> T getBean(String name, Class<T> clazz) {
        return getContext().getBean(name, clazz);
    }


    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        context = applicationContext;
    }
}
