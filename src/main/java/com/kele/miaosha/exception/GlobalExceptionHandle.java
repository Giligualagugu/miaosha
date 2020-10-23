package com.kele.miaosha.exception;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;


@Slf4j
@ControllerAdvice
public class GlobalExceptionHandle {

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(BizRuntimeException.class)
    public KeleResult<Object> handleBizRuntimeException(BizRuntimeException exception) {

        log.error("业务异常:", exception);

        return new KeleResult<>().fail(exception.getErrorCode(), exception.getMessage());
    }
}
