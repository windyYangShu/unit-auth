package com.metric.auth.core.persistence.dto;

import com.metric.auth.core.persistence.entity.SysAcl;
import org.springframework.beans.BeanUtils;

/**
 * @description:
 * @author: YangShu
 * @create: 2024-07-06
 **/
public class AclDto extends SysAcl {

    // 是否要默认选中
    private boolean checked = false;

    // 是否有权限操作
    private boolean hasAcl = false;

    public static AclDto adapt(SysAcl acl) {
        AclDto dto = new AclDto();
        BeanUtils.copyProperties(acl, dto);
        return dto;
    }
}
