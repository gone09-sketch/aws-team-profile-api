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

        log.warn("ServiceException 발생: {}", e.getMessage());

        return ResponseEntity.status(e.getStatus()).body(body);
    }
}
