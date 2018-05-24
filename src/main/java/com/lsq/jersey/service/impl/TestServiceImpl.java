package com.lsq.jersey.service.impl;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.lsq.jersey.api.page.PageResult;
import com.lsq.jersey.api.response.Response;
import com.lsq.jersey.api.response.ResponseStatusEnum;
import com.lsq.jersey.dao.po.Test;
import com.lsq.jersey.dao.TestDao;
import com.lsq.jersey.service.TestService;
import com.lsq.jersey.api.request.TestRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;


/**
 * 
 * TestMapper基本业务实现类
 * 
 **/

@Service
public class TestServiceImpl implements TestService {

    /********************************* 自动生成器代码start ******************************************/
    @Autowired
    private TestDao testDao;

    /** 主键查询entity **/
    public Test selectByPrimaryKey(Long id) {
        return testDao.selectByPrimaryKey(id);
    }

    /** 过滤查询entity **/
    public List<Test> selectFilter(TestRequest test) {
        return testDao.selectFilter(test);
    }

    /** 主键删除entity **/
    public int deleteByPrimaryKey(Long id) {
        return testDao.deleteByPrimaryKey(id);
    }

    /** 添加entity 返回记录条数 **/
    public int insert(Test test) {
        return testDao.insert(test);
    }

    /** 添加entity 返回主键Id **/
    public Long insertGetId(TestRequest test) {
        testDao.insertGetId(test);
        return test.getId();
    }

    /** 添加entity（匹配有值的字段） **/
    public int insertSelective(TestRequest test) {
        return testDao.insertSelective(test);
    }

    /** 修 改entity（匹配有值的字段） **/
    public int updateByPrimaryKeySelective(Test test) {
        return testDao.updateByPrimaryKeySelective(test);
    }

    /** 修 改entity全修改 **/
    public int updateByPrimaryKey(Test test) {
        return testDao.updateByPrimaryKey(test);
    }

    /** 批量插入或修改 **/
    public Integer batchInsertOrUpdate(List<Test> list) {
        return testDao.batchInsertOrUpdate(list);
    }
    /********************************* 自动生成器代码end ********************************************/

    public PageResult selectPage(TestRequest test) {
        PageHelper.startPage(test.getPageNum(), test.getPageSize());
        return PageResult.renderPageHelper(new PageInfo<Test>(selectFilter(test)));
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Response testTransactional(){
        Test test = new Test();
        test.setId(1L);
        test.setTestString("我勒个ca");
        updateByPrimaryKeySelective(test);

        TestRequest request = new TestRequest();
        request.setId(4L);
        request.setTestString("12");
        insertSelective(request);

        return Response.renderResponse(ResponseStatusEnum.SUCCESS, true);
    }
}
