package com.metric.auth.core.persistence.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.metric.auth.core.persistence.entity.SysUser;
import org.apache.ibatis.annotations.Param;

/**
 * @description:
 * @author: YangShu
 * @create: 2024-06-29
 **/
public interface SysUserMapper extends BaseMapper<SysUser> {

    /**
     * 根据用户名查询用户信息
     * @param userName
     * @return
     */
    SysUser getUserByName(@Param("userName") String userName);

    SysUser getUserByTelephone(@Param("telephone") String telephone);

    SysUser getUserByMail(@Param("mail") String mail);

    /**
     * 查询当前部门下用户数目
     * @param deptId
     * @return
     */
    int countByDeptId(@Param("deptId") int deptId);

    IPage<SysUser> getAllUser(IPage<SysUser> page);


}
