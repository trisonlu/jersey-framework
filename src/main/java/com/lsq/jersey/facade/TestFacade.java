package com.lsq.jersey.facade;

import com.lsq.jersey.dao.po.Test;
import com.lsq.jersey.service.TestService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;

/**
 * Created by trison on 2018/5/12.
 */
@Path("/test")
@Component
public class TestFacade {

    @Autowired
    private TestService testService;

    @GET
    @Path("/id/{id}")
    @Produces("application/json")
    public Test selectByPrimaryKey(@PathParam("id") Long id) {
        testService.selectByPrimaryKey(id);
        return new Test();
    }
}
