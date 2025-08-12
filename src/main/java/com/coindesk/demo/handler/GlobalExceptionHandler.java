package com.coindesk.demo.handler;

import com.coindesk.demo.dto.ErrorResultProxy;
import com.coindesk.demo.dto.response.ErrorResult;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
@Slf4j
public class GlobalExceptionHandler implements ErrorResultProxy {

    @ExceptionHandler(Exception.class)
    @ResponseBody
    public ResponseEntity<ErrorResult> handleApplicationException(Exception ex) {
        log.error(ex.getMessage(), ex);
        ErrorResult errorResult = newCreated(ex.getMessage());
        return new ResponseEntity<>(errorResult, HttpStatus.BAD_REQUEST);
    }
}
