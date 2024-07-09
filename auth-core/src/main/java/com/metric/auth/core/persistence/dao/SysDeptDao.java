package com.metric.auth.core.persistence.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.metric.auth.core.persistence.entity.SysDept;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @description:
 * @author: YangShu
 * @create: 2024-06-29
 **/
public interface SysDeptDao extends BaseMapper<SysDept> {

    /**
     * 条件查询部门
     * @param parentId
     * @param deptName
     * @param deptId
     * @return
     */
    List<SysDept> selectByNameAndParentId(
            @Param("parentId") Integer parentId,
            @Param("deptName") String deptName,
            @Param("deptId") Integer deptId);

    /**
     * 当前部门下级部门数目
     * @param deptId
     * @return
     */
    int countByParentId(@Param("deptId") int deptId);

    List<SysDept> getChildDeptListByLevel(@Param("level") String level);

    List<SysDept> getAllDept();

    void batchUpdateParentAndLevel(@Param("deptList") List<SysDept> deptList);
}
