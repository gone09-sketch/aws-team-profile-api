package com.cloudteamprofileapi.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class MemberCreateRequest {
    @NotBlank(message = "이름을 입력해주세요.")
    private String name;

    @NotNull(message = "나이를 입력해주세요")
    @Positive(message = "나이는 1 이상이어야 합니다.")
    private Integer age;

    @NotBlank(message = "mbti를 입력해주세요.")
    private String mbti;
}
