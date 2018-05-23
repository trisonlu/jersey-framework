package com.lsq.jersey.api.page;

import com.lsq.jersey.config.CustomPropertiesConfigurer;
import lombok.Data;


/**
 * Created by trison on 2018/5/21.
 */
@Data
public class Page {
    private int pageNum = Integer.valueOf(CustomPropertiesConfigurer.getCtxProp("page.defaultPageNum"));
    private int pageSize = Integer.valueOf(CustomPropertiesConfigurer.getCtxProp("page.defaultPageSize"));
}
