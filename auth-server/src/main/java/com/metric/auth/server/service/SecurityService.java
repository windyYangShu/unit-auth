package com.metric.auth.server.service;


import com.metric.auth.common.model.EncryptTypeConstant;
import com.metric.auth.common.utils.EncryptUtils;
import org.springframework.stereotype.Service;

/**
 * @description:
 * @author: YangShu
 * @create: 2024-07-01
 **/
@Service
public class SecurityService {

    public String encrypt(String type, String rawText) {
        switch (type.toUpperCase()) {
            case EncryptTypeConstant.SHA_256:
                return EncryptUtils.encryptSHA256(rawText);
            case EncryptTypeConstant.SHA_512:
                return EncryptUtils.encryptSHA512(rawText);
            case EncryptTypeConstant.AES:
                return EncryptUtils.encryptEAS(rawText);
            default:
                throw new RuntimeException("不支持该类型加密算法");
        }
    }
}
