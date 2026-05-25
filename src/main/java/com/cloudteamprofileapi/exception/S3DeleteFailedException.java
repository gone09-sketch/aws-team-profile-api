package com.cloudteamprofileapi.exception;

import com.cloudteamprofileapi.exception.base.ServiceException;
import org.springframework.http.HttpStatus;

public class S3DeleteFailedException extends ServiceException {

    public S3DeleteFailedException() {
        super(HttpStatus.INTERNAL_SERVER_ERROR, "프로필 이미지 삭제에 실패했습니다.");
    }
}