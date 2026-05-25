package com.cloudteamprofileapi.dto;

import lombok.Getter;

@Getter
public class ProfileImageResponse {

    private final Long memberId;
    private final String profileImageKey;
    private final String imageUrl;

    public ProfileImageResponse(Long memberId, String profileImageKey, String imageUrl) {
        this.memberId = memberId;
        this.profileImageKey = profileImageKey;
        this.imageUrl = imageUrl;
    }
}