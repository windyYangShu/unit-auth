package com.metric.auth.core.persistence.entity;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;

/**
 * @description:
 * @author: YangShu
 * @create: 2024-06-30
 **/
@TableName("t_auth_sys_app")
@Data
public class SysAuthApp implements Serializable {

    @TableId
    private String appId;

    private String appName;
}
