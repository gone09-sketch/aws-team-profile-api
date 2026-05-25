package com.cloudteamprofileapi.controller;

import com.cloudteamprofileapi.dto.MemberCreateRequest;
import com.cloudteamprofileapi.dto.MemberCreateResponse;
import com.cloudteamprofileapi.dto.MemberPatchRequest;
import com.cloudteamprofileapi.dto.MemberResponse;
import com.cloudteamprofileapi.dto.ProfileImageResponse;
import com.cloudteamprofileapi.dto.ProfileImageUploadResponse;
import com.cloudteamprofileapi.service.MemberService;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/api/members")
public class MemberController {

    private final MemberService memberService;

    public MemberController(MemberService memberService) {
        this.memberService = memberService;
    }

    // 멤버 등록
    @PostMapping
    public ResponseEntity<MemberCreateResponse> createMember(
            @RequestBody @Valid MemberCreateRequest memberCreateRequest) {

        log.info("[API - LOG] 멤버 추가 요청");
        MemberCreateResponse memberCreateResponse = memberService.createMember(memberCreateRequest);

        return ResponseEntity.status(HttpStatus.CREATED).body(memberCreateResponse);
    }

    // 멤버 전체 조회
    @GetMapping
    public ResponseEntity<List<MemberResponse>> findAllMembers() {

        log.info("[API - LOG] 멤버 전체 조회 요청");
        List<MemberResponse> memberGetResponseList = memberService.findAllMembers();
        return ResponseEntity.status(HttpStatus.OK).body(memberGetResponseList);
    }

    // 멤버 단건 조회
    @GetMapping("/{memberId}")
    public ResponseEntity<MemberResponse> findOneMember(
            @PathVariable Long memberId) {

        log.info("[API - LOG] 멤버 단건 조회 요청. memberId={}", memberId);
        MemberResponse memberGetResponse = memberService.findOneMember(memberId);
        return ResponseEntity.status(HttpStatus.OK).body(memberGetResponse);
    }

    // 멤버 정보 수정
    @PatchMapping("/{memberId}")
    public ResponseEntity<MemberResponse> updateMember(
            @RequestBody @Valid MemberPatchRequest memberPatchRequest,
            @PathVariable Long memberId) {

        log.info("[API - LOG] 멤버 정보 수정 요청. memberId={}", memberId);
        MemberResponse memberPatchResponse = memberService.updateMember(memberPatchRequest, memberId);
        return ResponseEntity.status(HttpStatus.OK).body(memberPatchResponse);
    }

    // 멤버 삭제
    @DeleteMapping("/{memberId}")
    public ResponseEntity<Void> deleteMember(@PathVariable Long memberId) {

        log.info("[API - LOG] 멤버 삭제 요청. memberId={}", memberId);
        memberService.deleteMember(memberId);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

    // 프로필 이미지 업로드
    @PostMapping("/{memberId}/profile-image")
    public ResponseEntity<ProfileImageUploadResponse> uploadProfileImage(
            @PathVariable Long memberId,
            @RequestPart("image") MultipartFile image
    ) {
        log.info("[API - LOG] 프로필 이미지 업로드 요청. memberId={}", memberId);

        ProfileImageUploadResponse response = memberService.uploadProfileImage(memberId, image);

        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    // 프로필 이미지 Presigned URL 조회
    @GetMapping("/{memberId}/profile-image")
    public ResponseEntity<ProfileImageResponse> getProfileImage(
            @PathVariable Long memberId
    ) {
        log.info("[API - LOG] 프로필 이미지 조회 요청. memberId={}", memberId);

        ProfileImageResponse response = memberService.getProfileImage(memberId);

        return ResponseEntity.status(HttpStatus.OK).body(response);
    }
}