package com.metric.auth.server.service;

import com.google.common.base.Preconditions;
import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.Lists;
import com.google.common.collect.Multimap;
import com.metric.auth.common.exceptions.BadRequestException;
import com.metric.auth.common.utils.LevelUtils;
import com.metric.auth.core.persistence.dao.SysDeptDao;
import com.metric.auth.core.persistence.dto.SysDeptDTO;
import com.metric.auth.core.persistence.entity.SysDept;
import com.metric.auth.core.persistence.mapper.SysUserMapper;
import com.metric.auth.server.controller.vo.BelongReqParam;
import com.metric.auth.server.controller.vo.DeptParam;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.logging.log4j.util.Strings;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

/**
 * @description:
 * @author: YangShu
 * @create: 2024-06-30
 **/
@Service
public class SysDeptService {

    @Autowired
    private SysDeptDao sysDeptDao;
    @Autowired
    private SysUserMapper sysUserMapper;


    @Transactional(rollbackFor = Exception.class)
    public void createDept(DeptParam deptParam) {
        if (checkDeptExist(deptParam.getParentId(), deptParam.getName(), deptParam.getId())) {
            throw new BadRequestException("同一层级下存在相同名称的部门");
        }
        SysDept sysDept = SysDept.builder()
                .name(deptParam.getName()).parentId(deptParam.getParentId())
                .seq(deptParam.getSeq()).remark(deptParam.getRemark()).build();
        sysDept.setLevel(LevelUtils.calculateLevel(getLevel(deptParam.getParentId()), deptParam.getParentId()));
        // todo 这里涉及到token
        sysDept.setOperator("test");
        sysDept.setOperateIp("123");
        sysDeptDao.insert(sysDept);
    }

    @Transactional(rollbackFor = Exception.class)
    public void deleteById(List<Integer> ids) {
        if (CollectionUtils.isNotEmpty(ids)){
            ids.forEach(i -> {
                SysDept sysDept = sysDeptDao.selectById(i);
                Preconditions.checkNotNull(sysDept, "待删除的部门不存在");
                if (sysDeptDao.countByParentId(i) > 0) {
                    throw new BadRequestException("当前部门下面有子部门，无法删除");
                }
                if (sysUserMapper.countByDeptId(i) > 0) {
                    throw new BadRequestException("当前部门下面有用户，无法删除");
                }
                sysDeptDao.deleteById(i);
            });
        }
    }

    private String getLevel(Integer deptId) {
        SysDept dept = sysDeptDao.selectById(deptId);
        if (dept == null) {
            return Strings.EMPTY;
        }
        return dept.getLevel();
    }

    private boolean checkDeptExist(Integer parentId, String deptName, Integer deptId) {
        return CollectionUtils
                .isEmpty(sysDeptDao.selectByNameAndParentId(parentId, deptName, deptId)) ? false : true;
    }

    public void updateDept(DeptParam deptParam) {
        if (checkDeptExist(deptParam.getParentId(), deptParam.getName(), deptParam.getId())) {
            throw new BadRequestException("同一层级下存在相同名称的部门");
        }
        SysDept sysDeptOld = sysDeptDao.selectById(deptParam.getId());
        Preconditions.checkNotNull(sysDeptOld, "待更新的部门不存在");
        SysDept sysDeptNew = SysDept.builder().id(deptParam.getId()).name(deptParam.getName())
                .parentId(deptParam.getParentId()).seq(deptParam.getSeq()).remark(deptParam.getRemark())
                .build();
        sysDeptNew.setLevel(LevelUtils.calculateLevel(getLevel(deptParam.getParentId()), deptParam.getParentId()));
        sysDeptNew.setOperator("test");
        sysDeptNew.setOperateIp("123");
        // 更新子部门层级
        updateWithChild(sysDeptOld, sysDeptNew);
    }

    @Transactional(rollbackFor = Exception.class)
    public void updateWithChild(SysDept sysDeptOld, SysDept sysDeptNew) {
        if (!sysDeptNew.getLevel().equalsIgnoreCase(sysDeptOld.getLevel())) {
            // todo SQL前缀查询考虑是否能够进行优化
            List<SysDept> deptList = sysDeptDao
                    .getChildDeptListByLevel(LevelUtils.calculateLevel(sysDeptOld.getLevel(), sysDeptOld.getId()));
            if (CollectionUtils.isNotEmpty(deptList)) {
                deptList.stream().forEach(d ->
                        d.setLevel(d.getLevel().replaceAll(sysDeptOld.getLevel(), sysDeptNew.getLevel()))
                );
            }
            sysDeptDao.batchUpdateParentAndLevel(deptList);
        }
        sysDeptDao.updateById(sysDeptNew);
    }

    // todo 采用map list生成目录树，这一块可以写一下作为blog
    public List<SysDeptDTO> getDeptTree() {
        List<SysDeptDTO> deptDTOS = new ArrayList<>();
        List<SysDept> deptList = sysDeptDao.getAllDept();
        if (CollectionUtils.isNotEmpty(deptList)) {
            deptList.forEach(d -> deptDTOS.add(new SysDeptDTO().convert(d)));
        }
        return deptListToTree(deptDTOS);
    }

    public List<SysDeptDTO> deptListToTree(List<SysDeptDTO> deptDTOList) {
        if (CollectionUtils.isEmpty(deptDTOList)) {
            return Lists.newArrayList();
        }

        // Map<String, List<SysDept>> level [obj1,obj2]
        Multimap<String, SysDeptDTO> levelDeptMap = ArrayListMultimap.create();
        List<SysDeptDTO> rootList = Lists.newArrayList();
        // 处理头节点
        deptDTOList.stream()
                .peek(d -> levelDeptMap.put(d.getLevel(), d))
                .filter(d -> LevelUtils.ROOT.equals(d.getLevel()))
                .forEach(rootList::add);

        // 按照seq从小到大排序
        Collections.sort(rootList, deptSeqComparator);

        //递归生成树
        transformDeptTree(rootList, LevelUtils.ROOT, levelDeptMap);
        return rootList;
    }

    private void transformDeptTree(List<SysDeptDTO> deptDTOList,
                                   String level, Multimap<String, SysDeptDTO> levelDeptMap) {
        for (int i = 0; i < deptDTOList.size(); i++) {
            SysDeptDTO deptDTO = deptDTOList.get(i);
            String nextLevel = LevelUtils.calculateLevel(level, deptDTO.getId());
            List<SysDeptDTO> tempDeptList = (List<SysDeptDTO>) levelDeptMap.get(nextLevel);
            if (CollectionUtils.isNotEmpty(tempDeptList)) {
                // 处理当前层级
                Collections.sort(tempDeptList, deptSeqComparator);
                deptDTO.setDeptList(tempDeptList);
                // 递归处理下一层级
                transformDeptTree(tempDeptList, nextLevel, levelDeptMap);
            }
        }
    }

    private Comparator<SysDeptDTO> deptSeqComparator = new Comparator<SysDeptDTO>() {
        @Override
        public int compare(SysDeptDTO o1, SysDeptDTO o2) {
            return o1.getSeq() - o2.getSeq();
        }
    };


    public SysDeptDTO getDeptDetail(Integer id) {
        SysDept dept = sysDeptDao.selectById(id);
        if (dept != null) {
            return new SysDeptDTO().convert(dept);
        }
        return null;
    }

    @Transactional(rollbackFor = Exception.class)
    public void updateDeptParent(BelongReqParam reqParam) {
        SysDept targetDept = sysDeptDao.selectById(reqParam.getTargetId());
        List<SysDept> list = sysDeptDao.selectBatchIds(reqParam.getIds());
        if (targetDept != null && CollectionUtils.isNotEmpty(list)) {
            list.forEach(i -> {
                i.setParentId(targetDept.getId());
                i.setLevel(LevelUtils.calculateLevel(targetDept.getLevel(), targetDept.getId()));
            });
        }
        sysDeptDao.batchUpdateParentAndLevel(list);
    }
}
