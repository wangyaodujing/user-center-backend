package com.wangyao.usercenter.common;

import lombok.Getter;

/**
 * @author 王尧
 * @version 1.0
 */
@Getter
public enum ErrorCode {

    SUCCESS(0, "ok", ""),
    PARAMS_ERROE(40000, "请求参数错误", ""),
    NULL_ERROE(40001, "请求数据为空", ""),
    NOT_LOGIN(40100, "未登录", ""),
    NO_AUTH(40101, "无权限", ""),
    LOGIN_ERROR(40200,"登录异常",""),
    INNER_ERROR(50100, "内部错误", ""),
    SYSTEM_ERROR(50000,"系统异常","");

    private final Integer code;

    private final String message;

    private final String description;

    ErrorCode(Integer code, String message, String description) {
        this.code = code;
        this.message = message;
        this.description = description;
    }
}
