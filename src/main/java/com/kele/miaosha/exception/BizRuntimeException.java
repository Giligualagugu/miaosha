package com.kele.miaosha.exception;

import lombok.Data;

@Data
public class BizRuntimeException extends RuntimeException {

    private String errorCode;

    public BizRuntimeException(String messge) {
        super(messge);
        this.errorCode = "101";
    }

    public BizRuntimeException(String code, String message) {
        super(message);
        this.errorCode = code;
    }
}
