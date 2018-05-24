package com.lsq.jersey.facade;

import com.lsq.jersey.api.request.TestRequest;
import com.lsq.jersey.api.response.Response;
import com.lsq.jersey.api.response.ResponseStatusEnum;
import com.lsq.jersey.config.PropertiesConfig;
import com.lsq.jersey.service.TestService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;

/**
 * Created by trison on 2018/5/12.
 */
@Path("/test")
@Component
@Slf4j
public class TestFacade{

    @Autowired
    private TestService testService;

    @Autowired
    private PropertiesConfig propertiesConfig;

    @GET
    @Path("/id/{id}")
    @Produces("application/json")
    public Response id(@PathParam("id") Long id) {
        log.info("id:{}", id);
        return Response.renderResponse(ResponseStatusEnum.SUCCESS, testService.selectByPrimaryKey(id));
    }

    @POST
    @Path("/list")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces("application/json")
    public Response list(TestRequest test) {
        log.info("listTestRequest:{}", test);
        return Response.renderResponse(ResponseStatusEnum.SUCCESS, testService.selectPage(test));
    }

    @POST
    @Path("/transaction/test")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces("application/json")
    public Response testTransactional() {
        try {
            return testService.testTransactional();
        } catch (Exception e) {
            return Response.renderResponse(ResponseStatusEnum.SERVER_ERROR, e);
        }
    }

}
