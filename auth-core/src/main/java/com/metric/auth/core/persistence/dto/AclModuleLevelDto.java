package com.metric.auth.core.persistence.dto;

import com.google.common.collect.Lists;
import com.metric.auth.core.persistence.entity.SysAclModule;
import lombok.Data;
import org.springframework.beans.BeanUtils;

import java.util.List;

/**
 * @description:
 * @author: YangShu
 * @create: 2024-07-06
 **/
@Data
public class AclModuleLevelDto extends SysAclModule {
    private List<AclModuleLevelDto> aclModuleList = Lists.newArrayList();

    private List<AclDto> aclList = Lists.newArrayList();

    public static AclModuleLevelDto adapt(SysAclModule aclModule) {
        AclModuleLevelDto dto = new AclModuleLevelDto();
        BeanUtils.copyProperties(aclModule, dto);
        return dto;
    }
}
