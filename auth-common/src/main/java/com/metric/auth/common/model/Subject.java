package com.metric.auth.common.model;

import com.google.common.base.Strings;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.annotation.Nonnull;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

/**
 * @description: todo 这个实体类还需要修改
 * @author: YangShu
 * @create: 2024-07-09
 **/
@Data
@NoArgsConstructor
public class Subject {
    // OA账户id
    private String id = "";
    // OA账户名称
    private String name = "";
    // 客户端id
    private String appId = "";
    // 客户端名称
    private String appName = "";
    // 编码后的认证信息
    private String encodedAuthentication = "";
    //认证信息
    private String authentication = "";
    //这是其它属性，可以应用
    private Map<String, String> attributes = new HashMap<>();

    public Subject(String id, String appId) {
        this.id = id;
        this.appId = appId;
    }

    public void addAttribute(@Nonnull String key, @Nonnull String value) {
        attributes.put(key, value);
    }

    public Optional<String> getAttribute(@Nonnull String key) {
        String value = attributes.get(key);
        if (Strings.isNullOrEmpty(value)) {
            return Optional.empty();
        } else {
            return Optional.of(value);
        }
    }

    @Override
    public String toString() {
        return "["
                + "ID='" + id + '\''
                + ", name='" + name + '\''
                + ", AppID='" + appId + '\''
                + ", AppName='" + appName + '\''
                + ", EncodedAuth='" + encodedAuthentication + '\''
                + ']';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        Subject subject = (Subject) o;
        return (appId != null ? appId.equals(subject.appId) : subject.appId == null)
                && (id != null ? !id.equals(subject.id) : subject.id != null);
    }

    @Override
    public int hashCode() {
        int result = id != null ? id.hashCode() : 0;
        result = 31 * result + (appId != null ? appId.hashCode() : 0);
        return result;
    }
}
