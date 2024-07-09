package com.metric.auth.core.persistence.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.metric.auth.core.persistence.entity.SysRoleUser;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @description:
 * @author: YangShu
 * @create: 2024-07-07
 **/
public interface SysRoleUserMapper extends BaseMapper<SysRoleUser> {

    List<SysRoleUser> getRoleByUser(@Param("userId") Integer id);
}
