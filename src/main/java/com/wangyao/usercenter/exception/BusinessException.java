package com.wangyao.usercenter.exception;

import com.wangyao.usercenter.common.ErrorCode;
import lombok.Getter;

/**
 * 自定义异常类
 * @author 王尧
 * @version 1.0
 */
@Getter
public class BusinessException extends RuntimeException{

    private final Integer code;

    private final String description;

    public BusinessException(String message, Integer code, String description) {
        super(message);
        this.code = code;
        this.description = description;
    }

    public BusinessException(ErrorCode errorCode) {
        super(errorCode.getMessage());
        this.code = errorCode.getCode();
        this.description = errorCode.getDescription();
    }

    public BusinessException(ErrorCode errorCode, String description) {
        super(errorCode.getMessage());
        this.code = errorCode.getCode();
        this.description = description;
    }
}
