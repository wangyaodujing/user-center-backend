package com.wangyao.usercenter.common;

/**
 * @author 王尧
 * @version 1.0
 */
public class ResultUtil {

    public static <T> BaseResponse<T> success(T data) {
        return new BaseResponse<T>(200, data, "ok");
    }

    public static <T> BaseResponse<T> error(ErrorCode errorCode) {
        return new BaseResponse<T>(errorCode);
    }

    public static <T> BaseResponse<T> error(int code, String message, String description) {
        return new BaseResponse<T>(code, null, message, description);
    }

    public static <T> BaseResponse<T> error(ErrorCode errorCode, String message, String description) {
        return new BaseResponse<T>(errorCode.getCode(), null, message, description);
    }

    public static <T> BaseResponse<T> error(ErrorCode errorCode, String description) {
        return new BaseResponse<T>(errorCode.getCode(), null, errorCode.getMessage(), description);
    }
}
