package com.lsq.jersey.service;

import com.lsq.jersey.api.page.PageResult;
import com.lsq.jersey.api.response.Response;
import com.lsq.jersey.dao.po.Test;
import com.lsq.jersey.api.request.TestRequest;
import java.util.List;


/**
 * 
 * TestMapper基本业务接口类
 * 
 **/

public interface TestService {

    /********************************* 自动生成器代码start ******************************************/
    /** 主键查询entity **/
    public Test selectByPrimaryKey(Long id);

    /** 过滤查询entity **/
    public List<Test> selectFilter(TestRequest test);

    /** 主键删除entity **/
    public int deleteByPrimaryKey(Long id);

    /** 添加entity 返回记录条数 **/
    public int insert(Test test);

    /** 添加entity 返回主键Id **/
    public Long insertGetId(TestRequest test);

    /** 添加entity（匹配有值的字段） **/
    public int insertSelective(TestRequest test);

    /** 修 改entity（匹配有值的字段） **/
    public int updateByPrimaryKeySelective(Test test);

    /** 修 改entity全修改 **/
    public int updateByPrimaryKey(Test test);

    /** 批量插入或修改修改 **/
    public Integer batchInsertOrUpdate(List<Test> list);
    /********************************* 自动生成器代码end ********************************************/

    public PageResult selectPage(TestRequest test);

    public Response testTransactional();
}
