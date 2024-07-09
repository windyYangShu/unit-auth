package com.metric.auth.common.utils;

import org.apache.commons.lang3.StringUtils;

/**
 * @description:
 * @author: YangShu
 * @create: 2024-06-30
 **/
public class LevelUtils {

    public final static String SEPARATOR = ".";

    public final static String ROOT = "0";

    // 0
    // 0.1
    // 0.1.2
    // 0.1.3
    // 0.4
    public static String calculateLevel(String parentLevel, int parentId) {
        if (StringUtils.isBlank(parentLevel)) {
            return ROOT;
        } else {
            return StringUtils.join(parentLevel, SEPARATOR, parentId);
        }
    }
}
