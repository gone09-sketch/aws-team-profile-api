package com.cloudteamprofileapi.dto;

import com.cloudteamprofileapi.entity.Member;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.time.LocalDateTime;

@Getter
@RequiredArgsConstructor
public class MemberCreateResponse {

    private final Long id;
    private final String name;
    private final Integer age;
    private final String mbti;
    private final String profileImageKey;
    private final LocalDateTime createdAt;

    public static MemberCreateResponse from(Member member) {
        return new MemberCreateResponse(
                member.getId(),
                member.getName(),
                member.getAge(),
                member.getMbti(),
                member.getProfileImageKey(),
                member.getCreatedAt()
        );
    }
}
