package com.lsq.jersey.api.response;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * Created by trison on 2018/5/21.
 */
@AllArgsConstructor
@Getter
public enum ResponseStatusEnum {

    SUCCESS(200, "成功"),
    REQUEST_ERROR(400, "请求参数错误"),
    REQUEST_LIMIT(401, "访问太频繁"),
    SERVER_ERROR(500, "服务器内部错误"),
    REMOTE_FAIL(501, "请求远程服务异常"),
    TIME_OUT(504, "请求远程服务超时");

    private int code;
    private String desc;
}
