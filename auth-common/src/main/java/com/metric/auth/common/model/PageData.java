package com.metric.auth.common.model;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * @description:
 * @author: YangShu
 * @create: 2024-07-03
 **/
@Data
@NoArgsConstructor
public final class PageData<T> {

    // 总记录数
    private long total = 0;

    // 每页记录数
    private int pageSize = 10;

    // 当前页码
    private long page = 1;

    private List<T> data;

    public PageData(PageData<Void> pageData) {
        this.page = Math.max(pageData.getPage(), 1);
        this.pageSize = pageData.getPageSize();
    }
}

