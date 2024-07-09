package com.metric.auth.server.service;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.google.common.base.Preconditions;
import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.Lists;
import com.google.common.collect.Multimap;
import com.metric.auth.common.exceptions.BadRequestException;
import com.metric.auth.common.utils.BeanCopierUtils;
import com.metric.auth.common.utils.LevelUtils;
import com.metric.auth.core.persistence.dao.SysAclModuleDao;
import com.metric.auth.core.persistence.dto.AclModuleLevelDto;
import com.metric.auth.core.persistence.entity.SysAcl;
import com.metric.auth.core.persistence.entity.SysAclModule;
import com.metric.auth.core.persistence.mapper.SysAclMapper;
import com.metric.auth.core.persistence.mapper.SysAclModuleMapper;
import com.metric.auth.server.controller.vo.SysAclModuleParam;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

//import jdk.internal.util.Preconditions;

/**
 * @description:
 * @author: YangShu
 * @create: 2024-07-05
 **/
@Service
public class SysAclModuleService {

    @Autowired
    private SysAclModuleMapper sysAclModuleMapper;
    @Autowired
    private SysAclMapper sysAclMapper;
    @Autowired
    private SysAclModuleDao sysAclModuleDao;

    @Transactional(rollbackFor = Exception.class)
    public void createAclModule(SysAclModuleParam param) {
        if(checkAclModuleExist(param.getParentId(), param.getName(), param.getId())) {
            throw new BadRequestException("同一层级下存在相同名称的权限模块");
        }

        SysAclModule aclModule = SysAclModule.builder().name(param.getName()).parentId(param.getParentId())
                .seq(param.getSeq()).status(param.getStatus()).remark(param.getRemark()).build();
        aclModule.setLevel(LevelUtils.calculateLevel(getLevel(param.getParentId()), param.getParentId()));
//        aclModule.setOperator(RequestHolder.getCurrentUser().getUsername());
//        aclModule.setOperateIp(IpUtil.getRemoteIp(RequestHolder.getCurrentRequest()));
        aclModule.setOperateTime(new Date());

        sysAclModuleMapper.insert(aclModule);
    }

    private String getLevel(Integer id) {
        SysAclModule aclModule = sysAclModuleMapper.selectById(id);
        if (aclModule == null) {
            return null;
        }
        return aclModule.getLevel();
    }

    private boolean checkAclModuleExist(Integer parentId, String name, Integer id) {
        return CollectionUtils
                .isEmpty(sysAclModuleMapper.selectByNameAndParentId(parentId, name, id)) ? false : true;
    }

    @Transactional(rollbackFor = Exception.class)
    public void deleteAclModule(List<Integer> ids) {
        Preconditions.checkArgument(CollectionUtils.isEmpty(ids), "待删除模块列表为空");
        List<SysAclModule> sysAclModules = sysAclModuleMapper.selectBatchIds(ids);
        if (ids.size() != sysAclModules.size()) {
            throw new BadRequestException("待删除的权限模块不存在，无法删除");
        }
        List<Integer> listIds = sysAclModules.stream().map(SysAclModule::getId).collect(Collectors.toList());
        LambdaQueryWrapper<SysAclModule> queryWrapper = new LambdaQueryWrapper();
        queryWrapper.in(SysAclModule::getParentId, listIds);
        if (CollectionUtils.isNotEmpty(sysAclModuleMapper.selectList(queryWrapper))) {
            throw new BadRequestException("当前模块下面有子模块，无法删除");
        }
        LambdaQueryWrapper<SysAcl> aclQueryWrapper = new LambdaQueryWrapper<>();
        aclQueryWrapper.in(SysAcl::getAclModuleId, listIds);
        if (CollectionUtils.isNotEmpty(sysAclMapper.selectList(aclQueryWrapper))) {
            throw new BadRequestException("当前模块下面有用户，无法删除");
        }
        sysAclModuleMapper.deleteBatchIds(ids);
    }



    public void updateAclModule(SysAclModuleParam param) {
        if(checkAclModuleExist(param.getParentId(), param.getName(), param.getId())) {
            throw new BadRequestException("同一层级下存在相同名称的权限模块");
        }
        SysAclModule sysAclModule = sysAclModuleMapper.selectById(param.getId());
        Preconditions.checkNotNull(sysAclModule, "待更新的权限模块不存在");
        SysAclModule aclModuleNew = new SysAclModule();
        BeanCopierUtils.copy(sysAclModule, aclModuleNew);
        aclModuleNew.setLevel(LevelUtils.calculateLevel(getLevel(param.getParentId()), param.getParentId()));
//        after.setOperator(RequestHolder.getCurrentUser().getUsername());
//        after.setOperateIp(IpUtil.getRemoteIp(RequestHolder.getCurrentRequest()));
        aclModuleNew.setOperateTime(new Date());
        updateWithChild(sysAclModule, aclModuleNew);
    }

    @Transactional(rollbackFor = Exception.class)
    public void updateWithChild(SysAclModule aclModuleOld, SysAclModule aclModuleNew) {
        if (!aclModuleOld.getLevel().equalsIgnoreCase(aclModuleNew.getLevel())) {
            // todo SQL前缀查询考虑是否能够进行优化
            List<SysAclModule> aclModules = sysAclModuleMapper
                    .getChildAclModuleListByLevel(LevelUtils.calculateLevel(aclModuleOld.getLevel(), aclModuleOld.getId()));
            if (CollectionUtils.isNotEmpty(aclModules)) {
                aclModules.stream().forEach(a ->
                        a.setLevel(a.getLevel().replaceAll(aclModuleOld.getLevel(), aclModuleNew.getLevel()))
                );
            }
            sysAclModuleMapper.batchUpdateParentAndLevel(aclModules);
        }
        sysAclModuleMapper.updateById(aclModuleNew);
    }


    public List<AclModuleLevelDto> getAclModuleList() {
        List<SysAclModule> aclModuleList = sysAclModuleMapper.getAllAclModule();
        List<AclModuleLevelDto> dtoList = aclModuleList.stream()
                .map(i -> AclModuleLevelDto.adapt(i)).collect(Collectors.toList());
        return aclModuleListToTree(dtoList);
    }

    public List<AclModuleLevelDto> aclModuleListToTree(List<AclModuleLevelDto> dtoList) {
        if (CollectionUtils.isEmpty(dtoList)) {
            return Lists.newArrayList();
        }

        // Map<String, List<SysDept>> level [obj1,obj2]
        Multimap<String, AclModuleLevelDto> aclModuleMap = ArrayListMultimap.create();
        List<AclModuleLevelDto> rootList = Lists.newArrayList();
        // 处理头节点
        dtoList.stream()
                .peek(d -> aclModuleMap.put(d.getLevel(), d))
                .filter(d -> LevelUtils.ROOT.equals(d.getLevel()))
                .forEach(rootList::add);

        // 按照seq从小到大排序
        Collections.sort(rootList, aclModuleSeqComparator);

        //递归生成树
        transformDeptTree(rootList, LevelUtils.ROOT, aclModuleMap);
        return rootList;
    }

    private void transformDeptTree(List<AclModuleLevelDto> dtoList,
                                   String level, Multimap<String, AclModuleLevelDto> levelDeptMap) {
        for (int i = 0; i < dtoList.size(); i++) {
            AclModuleLevelDto aclModuleLevelDto = dtoList.get(i);
            String nextLevel = LevelUtils.calculateLevel(level, aclModuleLevelDto.getId());
            List<AclModuleLevelDto> tempDeptList = (List<AclModuleLevelDto>) levelDeptMap.get(nextLevel);
            if (CollectionUtils.isNotEmpty(tempDeptList)) {
                // 处理当前层级
                Collections.sort(tempDeptList, aclSeqComparator);
                aclModuleLevelDto.setAclModuleList(tempDeptList);
                // 递归处理下一层级
                transformDeptTree(tempDeptList, nextLevel, levelDeptMap);
            }
        }
    }

    public Comparator<AclModuleLevelDto> aclModuleSeqComparator = new Comparator<AclModuleLevelDto>() {
        @Override
        public int compare(AclModuleLevelDto o1, AclModuleLevelDto o2) {
            return o1.getSeq() - o2.getSeq();
        }
    };

    public Comparator<AclModuleLevelDto> aclSeqComparator = new Comparator<AclModuleLevelDto>() {
        @Override
        public int compare(AclModuleLevelDto o1, AclModuleLevelDto o2) {
            return o1.getSeq() - o2.getSeq();
        }
    };

}
