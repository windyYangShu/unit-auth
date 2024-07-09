package com.metric.auth.core.persistence.entity;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

/**
 * @description:
 * @author: YangShu
 * @create: 2024-07-03
 **/
@TableName("t_auth_sys_role_acl")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SysAclRole {
    @TableId
    private Integer id;

    private Integer roleId;

    private Integer aclId;

    private String operator;

    private Date operateTime;

    private String operateIp;
}
