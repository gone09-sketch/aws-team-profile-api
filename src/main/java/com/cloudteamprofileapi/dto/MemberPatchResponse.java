package com.cloudteamprofileapi.dto;

import com.cloudteamprofileapi.entity.Member;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.time.LocalDateTime;

@Getter
@RequiredArgsConstructor
public class MemberPatchResponse {

    private final Long id;
    private final String name;
    private final Integer age;
    private final String mbti;
    private final LocalDateTime createdAt;
    private final LocalDateTime updatedAt;

    public static MemberPatchResponse from(Member member) {
        return new MemberPatchResponse(
                member.getId(),
                member.getName(),
                member.getAge(),
                member.getMbti(),
                member.getCreatedAt(),
                member.getUpdatedAt()
        );
    }
}
