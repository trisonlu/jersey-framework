package com.lsq.jersey.api.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


/**
 * Created by trison on 2018/5/21.
 */
@NoArgsConstructor
@AllArgsConstructor
@Data
public class Response{
    /**
     * 参考ResponseStatusEnum
     */
    private int status;

    /**
     * 响应消息
     */
    private String message;

    /**
     * 耗时毫秒数
     */
    private Long costTime;

    /**
     * 响应报文
     */
    private Object data;

    public static Response renderResponse(ResponseStatusEnum responseStatusEnum, Object obj) {
        Response response = new Response();
        response.setStatus(responseStatusEnum.getCode());
        if (responseStatusEnum.SUCCESS == responseStatusEnum) {
            response.setMessage(responseStatusEnum.getDesc());
            response.setData(obj);
        } else {
            response.setMessage(obj.toString());
        }
        return response;
    }

}
