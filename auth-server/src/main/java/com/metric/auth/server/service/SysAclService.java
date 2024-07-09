package com.metric.auth.server.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.google.common.base.Preconditions;
import com.metric.auth.common.exceptions.BadRequestException;
import com.metric.auth.common.model.PageData;
import com.metric.auth.common.utils.UUIDUtils;
import com.metric.auth.core.persistence.dao.SysAclDao;
import com.metric.auth.core.persistence.entity.SysAcl;
import com.metric.auth.core.persistence.mapper.SysAclMapper;
import com.metric.auth.server.controller.vo.AclParam;
import com.metric.auth.server.controller.vo.PageOrder;
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
public class SysAclService {

    @Autowired
    private SysAclDao sysAclDao;
    @Autowired
    private SysAclMapper sysAclMapper;

    private static final String PAGE_ORDER_ASC = "asc";

    @Transactional(rollbackFor = Exception.class)
    public void createAcl(AclParam param) {
        if (checkAclExist(param.getAclModuleId(), param.getName(), param.getId())) {
            throw new BadRequestException("同一权限模块存在相同的权限点名称");
        }
        SysAcl acl = SysAcl.builder().name(param.getName()).aclModuleId(param.getAclModuleId()).url(param.getUrl())
                .type(param.getType()).status(param.getStatus()).seq(param.getSeq()).remark(param.getRemark()).build();
        acl.setCode(UUIDUtils.getId());
//        acl.setOperator(RequestHolder.getCurrentUser().getUsername());
        acl.setOperateTime(new Date());
//        acl.setOperateIp(IpUtil.getRemoteIp(RequestHolder.getCurrentRequest()));
        sysAclMapper.insert(acl);
    }

    private boolean checkAclExist(Integer aclModuleId, String name, Integer id) {
        return CollectionUtils
                .isEmpty(sysAclMapper.selectByNameAndAclModule(aclModuleId, name, id)) ? false : true;
    }

    @Transactional(rollbackFor = Exception.class)
    public void updateAcl(AclParam param) {
        if (checkAclExist(param.getAclModuleId(), param.getName(), param.getId())) {
            throw new BadRequestException("同一权限模块存在相同的权限点名称");
        }
        SysAcl sysAcl = sysAclMapper.selectById(param.getId());
        Preconditions.checkNotNull(sysAcl, "待更新的权限点不存在");
        SysAcl sysAclNew = SysAcl.builder()
                .id(param.getId()).name(param.getName()).aclModuleId(param.getAclModuleId())
                .url(param.getUrl()).type(param.getType()).status(param.getStatus())
                .seq(param.getSeq()).remark(param.getRemark())
                .build();
        sysAclMapper.updateById(sysAclNew);
    }

    @Transactional(rollbackFor = Exception.class)
    public void deleteById(List<Integer> ids) {
        sysAclMapper.deleteBatchIds(ids);
    }

    public PageData<SysAcl> getAclList(String name, String type, String status,
                                       PageData<Void> pageData, List<PageOrder> orders) {
        Page<SysAcl> page = new Page<SysAcl>()
                .setCurrent(pageData.getPage())
                .setSize(pageData.getPageSize());
        QueryWrapper<SysAcl> queryWrapper = new QueryWrapper();
        queryWrapper
                .like(StringUtils.isNotEmpty(name), "name", name)
                .eq(StringUtils.isNotEmpty(type), "type", type)
                .eq(StringUtils.isNotEmpty(status), "status", status);
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

        Page<SysAcl> result = sysAclDao.page(page, queryWrapper);
        PageData<SysAcl> res = new PageData();
        res.setData(result.getRecords());
        res.setTotal(result.getTotal());
        res.setPage(result.getCurrent());
        return res;
    }
}
