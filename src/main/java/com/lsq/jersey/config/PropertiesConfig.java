package com.lsq.jersey.config;

import lombok.Data;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

/**
 * Created by trison on 2018/5/23.
 */
@Component
@Data
public class PropertiesConfig {
    @Value("${page.defaultPageNum:''}")
    public static int pageNum;
    @Value("${page.defaultPageSize:''}")
    public static int pageSize;
}
