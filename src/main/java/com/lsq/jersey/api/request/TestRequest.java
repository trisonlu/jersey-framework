package com.lsq.jersey.api.request;

import lombok.Data;


/**
 * 
 * 前台交互演示
 * 
 **/
@Data
public class TestRequest {

    /** 主键 **/
    private Long id;

    /** 字符串类型 **/
    private String testString;

    /** 金额Long类型转换 **/
    private Long testAmount;

    /** 小数bigdecimal类型转换 **/
    private java.math.BigDecimal testDouble;

    /** 状态 0 删除 1 初始 2 审核中 3 处理中。。。。 **/
    private Integer testEnum;

    /** 创建时间 **/
    private String createTime;

    /** 更新时间 **/
    private String updateTime;



}
