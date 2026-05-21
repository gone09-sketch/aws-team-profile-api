package com.cloudteamprofileapi.dto;

import jakarta.validation.constraints.Positive;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class MemberPatchRequest {

    private String name;

    @Positive(message = "나이는 1 이상이어야 합니다.")
    private Integer age;

    private String mbti;
}
