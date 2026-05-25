package com.cloudteamprofileapi.dto;

import com.cloudteamprofileapi.entity.Member;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import java.time.LocalDateTime;

@Getter
@RequiredArgsConstructor
public class MemberResponse {

    private final Long id;
    private final String name;
    private final Integer age;
    private final String mbti;
    private final String profileImageKey;
    private final LocalDateTime createdAt;
    private final LocalDateTime updatedAt;

    public static MemberResponse from(Member member) {
        return new MemberResponse(
                member.getId(),
                member.getName(),
                member.getAge(),
                member.getMbti(),
                member.getProfileImageKey(),
                member.getCreatedAt(),
                member.getUpdatedAt()
        );
    }
}
