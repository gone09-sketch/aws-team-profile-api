package com.cloudteamprofileapi.exception;

import com.cloudteamprofileapi.exception.base.ServiceException;
import org.springframework.http.HttpStatus;

public class S3UploadFailedException extends ServiceException {

    public S3UploadFailedException() {
        super(HttpStatus.INTERNAL_SERVER_ERROR, "프로필 이미지 업로드에 실패했습니다.");
    }
}