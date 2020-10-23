package com.kele.miaosha.config;

import com.kele.miaosha.exception.BizRuntimeException;
import com.kele.miaosha.exception.KeleResult;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;


@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandle {

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(BizRuntimeException.class)
    public KeleResult<Object> handleBizRuntimeException(BizRuntimeException exception) {
        log.error("业务异常:", exception);
        return new KeleResult<>().fail(exception.getErrorCode(), exception.getMessage());
    }
}
