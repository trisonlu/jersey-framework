package com.lsq.jersey.provider;

import com.lsq.jersey.api.response.Response;
import com.lsq.jersey.util.JSONUtil;
import com.sun.jersey.core.provider.AbstractMessageReaderWriterProvider;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.MultivaluedMap;
import javax.ws.rs.ext.Provider;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.lang.annotation.Annotation;
import java.lang.reflect.Type;

/**
 * Created by trison on 2018/5/21.
 */
@Provider
public class ResponseMessageWriterProvider extends AbstractMessageReaderWriterProvider<Response>{

    @Override
    public boolean isReadable(Class<?> aClass, Type type, Annotation[] annotations, MediaType mediaType) {
        return type == Response.class;
    }

    @Override
    public Response readFrom(Class<Response> aClass, Type type, Annotation[] annotations, MediaType mediaType, MultivaluedMap<String, String> multivaluedMap, InputStream inputStream) throws IOException, WebApplicationException {
        throw new IllegalArgumentException("Not implemented yet.");
    }

    @Override
    public boolean isWriteable(Class<?> aClass, Type type, Annotation[] annotations, MediaType mediaType) {
        return type == Response.class;
    }

    @Override
    @SuppressWarnings("unchecked")
    public void writeTo(Response response, Class<?> aClass, Type type, Annotation[] annotations, MediaType mediaType,
                        MultivaluedMap<String, Object> multivaluedMap, OutputStream outputStream)
            throws IOException, WebApplicationException {
        OutputStreamWriter writer = new OutputStreamWriter(outputStream, getCharset(mediaType));
        writer.write(JSONUtil.obj2JSONString(response));
        writer.flush();
    }
}
