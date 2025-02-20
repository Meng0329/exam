package com.example.exception;

import com.example.common.enums.ResultCodeEnum;

public class CustomException extends RuntimeException {

    private String code;
    private String msg;

    public CustomException(String code, String msg) {
        this.code = code;
        this.msg = msg;
    }

    public CustomException(ResultCodeEnum resultCodeEnum) {
        this.code = resultCodeEnum.code;
        this.msg = resultCodeEnum.msg;
    }

    public CustomException(String message) {
        super(message);
    }

    public CustomException(String message, Throwable cause) {
        super(message, cause);
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }
}
