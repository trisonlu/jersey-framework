package com.lsq.jersey.provider;

import com.lsq.jersey.api.response.Response;
import com.lsq.jersey.dao.po.Test;
import com.sun.jersey.api.json.JSONConfiguration;
import com.sun.jersey.api.json.JSONJAXBContext;
import lombok.extern.slf4j.Slf4j;

import javax.ws.rs.ext.ContextResolver;
import javax.ws.rs.ext.Provider;
import javax.xml.bind.JAXBContext;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

/**
 * Created by trison on 2018/5/14.
 */
@Provider
@Slf4j
public class JAXBContextResolver implements ContextResolver<JAXBContext> {

    private final JAXBContext jaxbContext;

    @SuppressWarnings("rawtypes")
    private final Set<Class> resultTypes;

    @SuppressWarnings("rawtypes")
    private final Class[] resultTypeArray = {
            Test.class
    };

    @SuppressWarnings("rawtypes")
    public JAXBContextResolver() throws Exception {
        this.resultTypes = new HashSet<Class>(Arrays.asList(resultTypeArray));
        this.jaxbContext = new JSONJAXBContext(JSONConfiguration.natural().build(), resultTypeArray);
    }

    @Override
    public JAXBContext getContext(Class<?> aClass) {
        return resultTypes.contains(aClass) ? jaxbContext : null;
    }
}
