package com.lsq.jersey.util;

import com.alibaba.fastjson.JSON;
import lombok.extern.slf4j.Slf4j;

/**
 * Created by trison on 2018/5/21.
 */
@Slf4j
public class JSONUtil {
    public static String obj2JSONString(Object obj) {
        if (obj == null) {
            return "{}";
        }
        return JSON.toJSONString(obj);
    }

}
