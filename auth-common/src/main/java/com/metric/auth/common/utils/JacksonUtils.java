package com.metric.auth.common.utils;

import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;

/**
 * @description:
 * @author: YangShu
 * @create: 2024-06-30
 **/
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class JacksonUtils {

    public static final ObjectMapper MAPPER =
            new ObjectMapper()
                .disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES)
                .disable(SerializationFeature.FAIL_ON_EMPTY_BEANS);

    /**
     * 工具类日志与业务日志分开
     */
    private static final Logger LOGGER = LoggerFactory.getLogger(JacksonUtils.class);

    public static String toJson(Object obj) {
        try {
            return MAPPER.writeValueAsString(obj);
        } catch (JsonProcessingException e) {
            LOGGER.error("", e);
            throw new RuntimeException("转换JSON失败");
        }
    }

    public static <T> T toObj(String json, Class<T> c) {
        try {
            return MAPPER.readValue(json, c);
        } catch (JsonProcessingException e) {
            LOGGER.error("", e);
            throw new RuntimeException("反序列化JSON失败");
        }
    }

    public static <T> T toObj(JsonParser json, Class<T> c) {
        try {
            return MAPPER.readValue(json, c);
        } catch (IOException e) {
            LOGGER.error("", e);
            throw new RuntimeException("反序列化JSON失败");
        }
    }

    public static <T> T toObj(JsonParser json, TypeReference<T> c) {
        try {
            return MAPPER.readValue(json, c);
        } catch (IOException e) {
            LOGGER.error("", e);
            throw new RuntimeException("反序列化JSON失败");
        }
    }

    public static <T> T toObj(String json, TypeReference<T> c) {
        try {
            JsonParser parser = new JsonFactory().createParser(json);
            return MAPPER.readValue(parser, c);
        } catch (IOException e) {
            LOGGER.error("", e);
            throw new RuntimeException("反序列化JSON失败");
        }
    }


    public static JsonNode readTree(String json) {
        try {
            return MAPPER.readTree(json);
        } catch (JsonProcessingException e) {
            LOGGER.error(json, e);
            throw new RuntimeException("处理JSON异常");
        }
    }
}
