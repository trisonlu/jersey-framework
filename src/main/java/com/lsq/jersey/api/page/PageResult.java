package com.lsq.jersey.api.page;

import com.github.pagehelper.PageInfo;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
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
     * 总页数
     */
    private int pages;

    /**
     * 当前列表记录
     */
    private List rows;


    public static PageResult renderPageHelper(PageInfo pageInfo) {
        PageResult pageResult = new PageResult();
        pageResult.setRows(new ArrayList());
        if (null != pageInfo && null != pageInfo.getList()) {
            pageResult.setRows(pageInfo.getList());
            pageResult.setPageNum(pageInfo.getPageNum());
            pageResult.setTotal(pageInfo.getTotal());
            pageResult.setPages(pageInfo.getPages());
            pageResult.setPageSize(pageInfo.getPageSize());
        }
        return pageResult;
    }
}
