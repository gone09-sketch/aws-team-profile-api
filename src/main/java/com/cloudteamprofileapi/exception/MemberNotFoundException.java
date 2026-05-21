package com.cloudteamprofileapi.exception;

import com.cloudteamprofileapi.exception.base.ServiceException;
import org.springframework.http.HttpStatus;

public class MemberNotFoundException extends ServiceException {

    public MemberNotFoundException() {
        super(HttpStatus.NOT_FOUND, "해당 멤버를 찾을 수 없습니다.");
    }
}