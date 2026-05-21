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
    private final LocalDateTime createdAt;

    public static MemberResponse from(Member member) {
        return new MemberResponse(
                member.getId(),
                member.getName(),
                member.getAge(),
                member.getMbti(),
                member.getCreatedAt()
        );
    }
}
