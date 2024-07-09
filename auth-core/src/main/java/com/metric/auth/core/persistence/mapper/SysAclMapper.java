package com.metric.auth.core.persistence.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.metric.auth.core.persistence.entity.SysAcl;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @description:
 * @author: YangShu
 * @create: 2024-07-03
 **/
public interface SysAclMapper extends BaseMapper<SysAcl> {

    List<SysAcl> selectByNameAndAclModule(
            @Param("aclModuleId") Integer aclModuleId, @Param("name") String name, @Param("id") Integer id);
}
