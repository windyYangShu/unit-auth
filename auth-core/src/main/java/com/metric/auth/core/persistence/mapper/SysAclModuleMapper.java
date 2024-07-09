package com.metric.auth.core.persistence.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.metric.auth.core.persistence.entity.SysAclModule;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @description:
 * @author: YangShu
 * @create: 2024-07-03
 **/
public interface SysAclModuleMapper extends BaseMapper<SysAclModule> {

    List<SysAclModule> selectByNameAndParentId(
            @Param("parentId") Integer parentId, @Param("name") String deptName, @Param("id") Integer id);

    List<SysAclModule> getChildAclModuleListByLevel(@Param("level") String level);

    void batchUpdateParentAndLevel(@Param("aclModules") List<SysAclModule> aclModules);

    List<SysAclModule> getAllAclModule();
}
