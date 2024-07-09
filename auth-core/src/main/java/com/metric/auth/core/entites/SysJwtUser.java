package com.metric.auth.core.entites;

import com.metric.auth.core.persistence.entity.SysUser;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @description:
 * @author: YangShu
 * @create: 2024-07-07
 **/
@Data
@NoArgsConstructor
@AllArgsConstructor
public class SysJwtUser {

    private String token;
    private SysUser user;
}
