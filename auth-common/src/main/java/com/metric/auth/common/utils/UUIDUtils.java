package com.metric.auth.common.utils;

import com.metric.auth.common.exceptions.GlobalException;
import com.metric.auth.common.log.Logger;

import java.lang.management.ManagementFactory;
import java.lang.management.RuntimeMXBean;
import java.math.BigInteger;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.nio.charset.Charset;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * @description:
 * @author: YangShu
 * @create: 2024-07-03
 **/
public class UUIDUtils {
    private static final Logger LOGGER = Logger.getLogger(UUIDUtils.class);

    private static final int ID_LENGTH = 32;
    private static final int RANDOM_VAL = 1000000000;
    private static final int SEQUENCE_MAX_VAL = 100000;
    private static String ip;
    private static String pid;
    private static AtomicInteger index = new AtomicInteger(0);

    static {
        try {
            ip = InetAddress.getLocalHost().toString();
        } catch (UnknownHostException e) {
            ip = String.format("%2.0f", Math.random() * RANDOM_VAL);
            LOGGER.error("", e);
        }
        //获得进程 ID
        RuntimeMXBean runtime = ManagementFactory.getRuntimeMXBean();
        String name = runtime.getName();
        int index = name.indexOf("@");
        if (index != -1) {
            pid = name.substring(0, index);
        } else {
            pid = String.format("%2.0f", Math.random() * RANDOM_VAL);
        }
    }

    /**
     * 生成一个唯一ID：IP+进程号+时间+序列,然后以32位的形式展示节省空间
     */
    public static String getId() {
        int sequence = index.getAndIncrement();
        if (sequence > SEQUENCE_MAX_VAL) {
            synchronized (index) {
                index.set(0);
            }
        }
        MessageDigest md;
        try {
            md = MessageDigest.getInstance("MD5");
        } catch (NoSuchAlgorithmException e) {
            LOGGER.error("获取MD5加密器失败", e);
            throw new GlobalException("获取唯一ID失败");
        }
        // 计算md5函数
        md.update((ip + pid + System.currentTimeMillis() + sequence)
                .getBytes(Charset.forName("UTF-8")));
        return new BigInteger(1, md.digest()).toString(ID_LENGTH);
    }
}
