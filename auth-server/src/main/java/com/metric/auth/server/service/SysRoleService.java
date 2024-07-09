package com.metric.auth.server.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.metric.auth.common.exceptions.BadRequestException;
import com.metric.auth.common.model.PageData;
import com.metric.auth.common.utils.BeanCopierUtils;
import com.metric.auth.core.persistence.dao.SysRoleDao;
import com.metric.auth.core.persistence.entity.SysRole;
import com.metric.auth.core.persistence.mapper.SysRoleMapper;
import com.metric.auth.server.controller.vo.PageOrder;
import com.metric.auth.server.controller.vo.RoleParam;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;

/**
 * @description:
 * @author: YangShu
 * @create: 2024-07-03
 **/
@Service
public class SysRoleService {

    @Autowired
    private SysRoleMapper sysRoleMapper;
    @Autowired
    private SysRoleDao sysRoleDao;

    private static final String PAGE_ORDER_ASC = "asc";

    @Transactional(rollbackFor = Exception.class)
    public void createRole(RoleParam roleParam) {
        checkRole(roleParam);
        SysRole role = SysRole.builder().name(roleParam.getName())
                .status(roleParam.getStatus()).type(roleParam.getType()).remark(roleParam.getRemark())
                .build();
//        role.setOperator(RequestHolder.getCurrentUser().getUsername());
//        role.setOperateIp(IpUtil.getRemoteIp(RequestHolder.getCurrentRequest()));
        role.setOperateTime(new Date());
        sysRoleMapper.insert(role);
    }

    public void checkRole(RoleParam roleParam) {
        if (checkNameExist(roleParam.getName(), roleParam.getId())) {
            throw new BadRequestException("角色名称已经存在");
        }
        if (checkMailExist(roleParam.getName(), roleParam.getId())) {
            throw new BadRequestException("邮箱已经存在");
        }
        if (checkTelephoneExist(roleParam.getName(), roleParam.getId())) {
            throw new BadRequestException("电话已经存在");
        }
    }

    private boolean checkNameExist(String name, Integer id) {
        return CollectionUtils
                .isEmpty(sysRoleMapper.selectByName(name, id)) ? false : true;
    }

    private boolean checkMailExist(String mail, Integer id) {
        return CollectionUtils
                .isEmpty(sysRoleMapper.selectByMail(mail, id)) ? false : true;
    }

    private boolean checkTelephoneExist(String telephone, Integer id) {
        return CollectionUtils
                .isEmpty(sysRoleMapper.selectByTelephone(telephone, id)) ? false : true;
    }


    @Transactional(rollbackFor = Exception.class)
    public void deleteRole(List<Integer> ids) {
        if (CollectionUtils.isEmpty(ids)) {
            return;
        }
        ids.forEach(i -> {
            SysRole role = sysRoleMapper.selectById(i);
            if (role == null) {
                throw new BadRequestException("待删除的角色不存在");
            }
        });
        sysRoleMapper.deleteBatchIds(ids);
    }

    @Transactional(rollbackFor = Exception.class)
    public void update(RoleParam param) {
        checkRole(param);
        SysRole sysRole = sysRoleMapper.selectById(param.getId());
        if (sysRole == null) {
            throw new BadRequestException("待更新的角色不存在");
        }
        SysRole roleNew = new SysRole();
        BeanCopierUtils.copy(param, roleNew);
        sysRoleMapper.updateById(roleNew);
    }

    public PageData<SysRole> getRoleList(String name, String type, String status,
                                         PageData<SysRole> pageData, List<PageOrder> orders) {
        Page<SysRole> page = new Page<SysRole>()
                .setCurrent(pageData.getPage())
                .setSize(pageData.getPageSize());
        QueryWrapper<SysRole> queryWrapper = new QueryWrapper();
        queryWrapper
                .like(StringUtils.isNotEmpty(name), "name" ,name)
                .eq(StringUtils.isNotEmpty(type), "type", type)
                .eq(StringUtils.isNotEmpty(status), "status", status);
        queryWrapper.like(StringUtils.isNotEmpty(name), "name" ,name);
        if (CollectionUtils.isNotEmpty(orders)) {
            orders.stream()
                    .filter(m -> StringUtils.isNotEmpty(m.getColumn()))
                    .forEach(i -> {
                        if (StringUtils.isNotEmpty(i.getOrder())
                                && i.getOrder().toLowerCase().equalsIgnoreCase(PAGE_ORDER_ASC)) {
                            queryWrapper.orderByDesc(i.getColumn());
                        } else {
                            queryWrapper.orderByAsc(i.getColumn());
                        }
                    });
        }

        Page<SysRole> result = sysRoleDao.page(page, queryWrapper);
        pageData.setData(result.getRecords());
        pageData.setTotal(result.getTotal());
        pageData.setPage(result.getCurrent());
        return pageData;
    }
}
