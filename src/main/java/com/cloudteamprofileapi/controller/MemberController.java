package com.cloudteamprofileapi.controller;

import com.cloudteamprofileapi.dto.MemberCreateRequest;
import com.cloudteamprofileapi.dto.MemberPatchRequest;
import com.cloudteamprofileapi.dto.MemberPatchResponse;
import com.cloudteamprofileapi.dto.MemberResponse;
import com.cloudteamprofileapi.service.MemberService;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@Slf4j
@RestController
@RequestMapping("/members")
public class MemberController {

    private final MemberService memberService;

    public MemberController(MemberService memberService) {
        this.memberService = memberService;
    }

    // 멤버 등록
    @PostMapping
    public ResponseEntity<MemberResponse> createMember(
            @RequestBody @Valid MemberCreateRequest memberCreateRequest) {

        log.info("[API - LOG] 멤버 추가 요청");
        MemberResponse memberCreateResponse = memberService.createMember(memberCreateRequest);

        return ResponseEntity.status(HttpStatus.CREATED).body(memberCreateResponse);
    }

    // 멤버 전체 조회
    @GetMapping
    public ResponseEntity<List<MemberResponse>> findAllMembers() {

        log.info("[API - LOG] 멤버 전체 조회 요청");
        List<MemberResponse> memberGetResponseList = memberService.findAllMembers();
        return ResponseEntity.status(HttpStatus.OK).body(memberGetResponseList);
    }

    // 멤버 부분 조회
    @GetMapping("/{memberId}")
    public ResponseEntity<MemberResponse> findOneMember(
            @PathVariable Long memberId) {

        log.info("[API - LOG] 멤버 단건 조회 요청. memberId={}", memberId);
        MemberResponse memberGetResponse = memberService.findOneMember(memberId);
        return ResponseEntity.status(HttpStatus.OK).body(memberGetResponse);
    }

    // 멤버 정보 수정
    @PatchMapping("/{memberId}")
    public ResponseEntity<MemberPatchResponse> updateMember(
            @RequestBody @Valid MemberPatchRequest memberPatchRequest,
            @PathVariable Long memberId) {

        log.info("[API - LOG] 멤버 정보 수정 요청. memberId={}", memberId);
        MemberPatchResponse memberPatchResponse = memberService.updateMember(memberPatchRequest, memberId);
        return ResponseEntity.status(HttpStatus.OK).body(memberPatchResponse);
    }

    // 멤버 삭제
    @DeleteMapping("/{memberId}")
    public ResponseEntity<Void> deleteMember(@PathVariable Long memberId) {

        log.info("[API - LOG] 멤버 삭제 요청. memberId={}", memberId);
        memberService.deleteMember(memberId);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }
}
