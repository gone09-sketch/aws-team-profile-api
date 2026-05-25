package com.cloudteamprofileapi.exception;

import com.cloudteamprofileapi.exception.base.ServiceException;
import org.springframework.http.HttpStatus;

public class ProfileImageNotFoundException extends ServiceException {

    public ProfileImageNotFoundException() {
        super(HttpStatus.NOT_FOUND, "등록된 프로필 이미지가 없습니다.");
    }
}