
package com.kele.miaosha.exception;


import lombok.Data;

import java.io.Serializable;

@Data
public class KeleResult<T> implements Serializable {

    private static final long serialVersionUID = -5757933746700705620L;

    private T result;
    private String message;
    private String code;
    private String cause;

    public KeleResult() {
    }

    public KeleResult(T result) {
        this.result = result;
    }

    public KeleResult(String code, String message) {
        this.code = code;
        this.message = message;
    }

    public KeleResult<T> result(T result) {
        this.result = result;
        return this;
    }

    public KeleResult<T> success() {
        this.code = "0";
        this.message = "ok";
        return this;
    }

    public KeleResult<T> fail() {
        this.code = "101";
        this.message = "fail";
        return this;
    }

    public KeleResult<T> fail(String message) {
        this.code = "101";
        this.message = message;
        return this;
    }

    public KeleResult<T> fail(String code, String message) {
        this.code = code;
        this.message = message;
        return this;
    }

    public KeleResult<T> cause(String cause) {
        this.cause = cause;
        return this;
    }

}
