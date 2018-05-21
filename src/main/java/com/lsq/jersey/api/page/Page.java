package com.lsq.jersey.api.page;

import lombok.Data;
import org.springframework.beans.factory.annotation.Value;

/**
 * Created by trison on 2018/5/21.
 */
@Data
public class Page {
    @Value("${page.defaultPageNum:''}")
    private int pageNum;
    @Value("${page.defaultPageSize:''}")
    private int pageSize;
}
