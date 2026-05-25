package com.cloudteamprofileapi.exception;

import com.cloudteamprofileapi.exception.base.ServiceException;
import org.springframework.http.HttpStatus;

public class InvalidImageFileException extends ServiceException {

    public InvalidImageFileException(String message) {
        super(HttpStatus.BAD_REQUEST, message);
    }
}