package com.metric.auth.core.persistence.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.metric.auth.core.persistence.entity.SysRole;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @description:
 * @author: YangShu
 * @create: 2024-07-03
 **/
public interface SysRoleMapper extends BaseMapper<SysRole> {

    List<SysRole> selectByName(@Param("name") String name, @Param("id") Integer id);

    List<SysRole> selectByMail(@Param("mail") String mail, Integer id);

    List<SysRole> selectByTelephone(@Param("telephone") String telephone, Integer id);

    List<SysRole> getRoleByUser(@Param("userId") Integer id);
}
