package com.metric.auth.common.utils;

import com.metric.auth.common.exceptions.GlobalException;
import com.metric.auth.common.model.EncryptTypeConstant;
import org.apache.commons.lang3.StringUtils;
import sun.misc.BASE64Decoder;
import sun.misc.BASE64Encoder;

import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;
import java.math.BigInteger;
import java.nio.charset.Charset;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

/**
 * @description:
 * @author: YangShu
 * @create: 2024-07-01
 **/
public class EncryptUtils {

    private static final String SHA_256 = "encryptSHA-256";
    private static final String SHA_512 = "encryptSHA-512";
    private static final String ENCODING_UTF_8 = "UTF-8";
    private static final int KEY_LENGTH = 32;
    private static final String PASSWORD = ""; // 加密秘钥
    private static final String KEY_WORD = "metric";

    /**
     * 加解密密钥, 外部可以
     */
    public static final String AES_DATA_SECURITY_KEY = "6D36F225EFB0BBA877B0CD30B7C68CEBEE9AE15D6839C1A77FC8EA30EADD2E1E677CC8A633BD7FE61C5DA565E16E3C1CD97B7F00865178320C1DC8C8DCF7B09F";

    /**
     * 算法/加密模式/填充方式
     */
    private static final String AES_PKCS5P = "AES/ECB/PKCS5Padding";

    private static final String AES_PERSON_KEY_SECURITY_KEY = "pisnyMyZYXuCNcRd";


    /**
     * 使用AES-ECB加密解密
     * @param str
     * @return
     */
    public static String encryptEAS(String str) {
        String key = encryptMD5(KEY_WORD);
        return encryptEAS(str, key);
    }

    public static String encryptEAS(String str, String key) {
        if (StringUtils.isEmpty(str)) {
            throw new GlobalException("待加密字符串不能为空");
        }
        if (key == null || key.length() != KEY_LENGTH) {
            throw new GlobalException("密钥长度错误");
        }
        try {
            SecretKeySpec skeySpec = new SecretKeySpec(key.getBytes(ENCODING_UTF_8), EncryptTypeConstant.AES);
            // "算法/模式/补码方式"
            Cipher cipher = Cipher.getInstance(AES_PKCS5P);
            cipher.init(Cipher.ENCRYPT_MODE, skeySpec);
            byte[] encrypted = cipher.doFinal(str.getBytes(ENCODING_UTF_8));
            // 此处使用BASE64做转码功能，同时能起到2次加密的作用。
            return new BASE64Encoder().encode(encrypted);
        } catch (Exception ex) {
            throw new GlobalException(ex);
        }

    }

    /**
     * 解密
     *
     * @param str 需要解密的字符串
     * @param key 密钥
     * @return
     */
    public static String decryptAES(String str, String key) {
        if (StringUtils.isEmpty(str)) {
            throw new GlobalException("待解密字符串不能为空");
        }
        if (key == null || key.length() != KEY_LENGTH) {
            throw new GlobalException("密钥长度错误");
        }
        try {
            SecretKeySpec skeySpec = new SecretKeySpec(key.getBytes(ENCODING_UTF_8), EncryptTypeConstant.AES);
            Cipher cipher = Cipher.getInstance(AES_PKCS5P);
            cipher.init(Cipher.DECRYPT_MODE, skeySpec);
            // 先用base64解密
            byte[] encrypted = new BASE64Decoder().decodeBuffer(str);
            try {
                byte[] original = cipher.doFinal(encrypted);
                String originalString = new String(original, ENCODING_UTF_8);
                return originalString;
            } catch (Exception e) {
                throw new GlobalException(e);
            }
        } catch (Exception ex) {
            throw new GlobalException(ex);
        }
    }

    public static String encryptSHA256(final String rawText) {
        return encryptSHA(rawText, SHA_256);
    }

    public static String encryptSHA512(final String rawText) {
        return encryptSHA(rawText, SHA_512);
    }

    public static String encryptMD5(String str) {
        if (StringUtils.isEmpty(str)) {
            throw new GlobalException("字符串不能为空");
        }
        try {
            MessageDigest md = MessageDigest.getInstance(EncryptTypeConstant.MD5);
            md.update(str.getBytes(Charset.forName(ENCODING_UTF_8)));
            return String.format("%032x", new BigInteger(1, md.digest()));
        } catch (NoSuchAlgorithmException e) {
            throw new GlobalException("MD5加密失败");
        }
    }

    /**
     * sha加密
     * @param rawText
     * @param type
     * @return
     */
    private static String encryptSHA(final String rawText, final String type) {
        if (StringUtils.isNotEmpty(rawText)) {
            try {
                MessageDigest messageDigest = MessageDigest.getInstance(type);
                messageDigest.update(rawText.getBytes());
                byte[] bytes = messageDigest.digest();
                String hexString = IntStream.range(0, bytes.length)
                        .mapToObj(i -> String.format("%02x", bytes[i] & 0xff))
                        .collect(Collectors.joining());
                return hexString;
            } catch (NoSuchAlgorithmException e) {
                e.printStackTrace();
            }
        }
        return null;
    }

}
