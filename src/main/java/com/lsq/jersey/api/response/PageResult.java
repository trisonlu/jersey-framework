package com.lsq.jersey.api.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * Created by trison on 2018/5/21.
 */
@NoArgsConstructor
@AllArgsConstructor
@Data
public class PageResult {
    /**
     * 当前页
     */
    private int pageNum;

    /**
     * 页大小
     */
    private int pageSize;

    /**
     * 总记录数
     */
    private long total;

    /**
     * 当前列表记录
     */
    private List rows;
}
