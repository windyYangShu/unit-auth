package com.metric.auth.core.persistence.dao;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.metric.auth.core.persistence.entity.SysUser;
import com.metric.auth.core.persistence.mapper.SysUserMapper;
import org.springframework.stereotype.Service;

/**
 * @description:
 * @author: YangShu
 * @create: 2024-07-03
 **/
@Service
public class SysUserDao extends ServiceImpl<SysUserMapper, SysUser> {
}
