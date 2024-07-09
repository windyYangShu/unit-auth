package com.metric.auth.server.controller.vo;

import lombok.Data;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.List;

/**
 * @description:
 * @author: YangShu
 * @create: 2024-07-02
 **/
@Data
public class BelongReqParam {

    @NotNull(message = "目标id不能为空")
    private Integer targetId;

    @Size(min = 1, message = "未选择待操作对象")
    private List<Integer> ids;
}
