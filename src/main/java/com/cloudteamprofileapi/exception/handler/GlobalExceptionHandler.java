package com.cloudteamprofileapi.exception.handler;

import com.cloudteamprofileapi.exception.base.ServiceException;
import com.cloudteamprofileapi.exception.dto.ErrorResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(ServiceException.class)
    public ResponseEntity<ErrorResponse> handlerServiceException(ServiceException e) {

        String message = e.getMessage();
        HttpStatus status = e.getStatus();
        ErrorResponse body = new ErrorResponse(e.getStatus(), e.getMessage());

        log.error("[API - ERROR] 서비스 예외 발생. status={}, message={}",
                e.getStatus(),
                e.getMessage(),
                e
        );

        return ResponseEntity.status(e.getStatus()).body(body);
    }
}
