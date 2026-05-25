package com.cloudteamprofileapi.dto;

import lombok.Getter;

@Getter
public class ProfileImageUploadResponse {

    private final Long memberId;
    private final String profileImageKey;
    private final String imageUrl;

    public ProfileImageUploadResponse(Long memberId, String profileImageKey, String imageUrl) {
        this.memberId = memberId;
        this.profileImageKey = profileImageKey;
        this.imageUrl = imageUrl;
    }
}