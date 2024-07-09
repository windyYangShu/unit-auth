package com.metric.auth.core.persistence.dto;

import com.metric.auth.common.utils.BeanCopierUtils;
import com.metric.auth.core.persistence.entity.SysDept;
import lombok.Data;

import java.util.List;

/**
 * @description:
 * @author: YangShu
 * @create: 2024-07-02
 **/
@Data
public class SysDeptDTO extends SysDept {

    private List<SysDeptDTO> deptList;

    public SysDeptDTO convert(SysDept sysDept) {
        SysDeptDTO deptDTO = new SysDeptDTO();
        BeanCopierUtils.copy(sysDept, deptDTO);
        return deptDTO;
    }
}
