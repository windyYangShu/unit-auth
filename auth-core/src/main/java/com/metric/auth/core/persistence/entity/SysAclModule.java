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
 * @create: 2024-07-05
 **/
@TableName("t_auth_sys_acl_module")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SysAclModule {
    @TableId
    private Integer id;

    private String name;

    private Integer parentId;

    private String level;

    private Integer seq;

    private Integer status;

    private String remark;

    private String operator;

    private Date operateTime;

    private String operateIp;
}
