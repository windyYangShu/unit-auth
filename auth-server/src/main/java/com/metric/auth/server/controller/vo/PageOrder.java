package com.metric.auth.server.controller.vo;

import lombok.Data;

import java.io.Serializable;

/**
 * @description:
 * @author: YangShu
 * @create: 2024-07-03
 **/
@Data
public class PageOrder implements Serializable {

    private static final long serialVersionUID = 1L;

    //需要进行排序的字段
    private String column;

    private String order;
}