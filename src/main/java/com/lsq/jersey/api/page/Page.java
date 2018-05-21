package com.lsq.jersey.api.page;

import lombok.Data;

/**
 * Created by trison on 2018/5/21.
 */
@Data
public class Page {
    private int pageNum = 1;
    private int pageSize = 20;
}
