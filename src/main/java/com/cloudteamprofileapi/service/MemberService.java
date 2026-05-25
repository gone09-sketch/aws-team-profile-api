package com.cloudteamprofileapi.service;

import com.cloudteamprofileapi.dto.MemberCreateRequest;
import com.cloudteamprofileapi.dto.MemberCreateResponse;
import com.cloudteamprofileapi.dto.MemberPatchRequest;
import com.cloudteamprofileapi.dto.MemberResponse;
import com.cloudteamprofileapi.dto.ProfileImageResponse;
import com.cloudteamprofileapi.dto.ProfileImageUploadResponse;
import com.cloudteamprofileapi.entity.Member;
import com.cloudteamprofileapi.exception.MemberNotFoundException;
import com.cloudteamprofileapi.exception.ProfileImageNotFoundException;
import com.cloudteamprofileapi.repository.MemberRepository;
import lombok.Getter;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Getter
@Service
public class MemberService {

    private final MemberRepository memberRepository;
    private final S3ImageService s3ImageService;

    public MemberService(MemberRepository memberRepository, S3ImageService s3ImageService) {
        this.memberRepository = memberRepository;
        this.s3ImageService = s3ImageService;
    }

    // 멤버 등록
    @Transactional
    public MemberCreateResponse createMember(MemberCreateRequest memberCreateRequest) {

        Member newMember = new Member(
                memberCreateRequest.getName(),
                memberCreateRequest.getAge(),
                memberCreateRequest.getMbti(),
                null
        );

        Member savedMember = memberRepository.save(newMember);
        return MemberCreateResponse.from(savedMember);
    }

    // 멤버 전체 조회
    @Transactional(readOnly = true)
    public List<MemberResponse> findAllMembers() {

        List<Member> allMembers = memberRepository.findAll();

        return allMembers.stream()
                .map(MemberResponse::from)
                .toList();
    }

    // 멤버 단건 조회
    @Transactional(readOnly = true)
    public MemberResponse findOneMember(Long memberId) {

        Member member = getMemberById(memberId);
        return MemberResponse.from(member);
    }

    // 멤버 정보 수정
    @Transactional
    public MemberResponse updateMember(MemberPatchRequest memberPatchRequest, Long memberId) {

        Member member = getMemberById(memberId);

        Member updatedMember = member.update(
                memberPatchRequest.getName(),
                memberPatchRequest.getAge(),
                memberPatchRequest.getMbti()
        );

        return MemberResponse.from(updatedMember);
    }

    // 멤버 삭제
    @Transactional
    public void deleteMember(Long memberId) {

        Member member = getMemberById(memberId);

        s3ImageService.deleteProfileImage(member.getProfileImageKey());

        memberRepository.delete(member);
    }

    // 프로필 이미지 업로드
    @Transactional
    public ProfileImageUploadResponse uploadProfileImage(Long memberId, MultipartFile image) {

        Member member = getMemberById(memberId);

        String oldProfileImageKey = member.getProfileImageKey();

        String newProfileImageKey = s3ImageService.uploadProfileImage(memberId, image);

        member.updateProfileImageKey(newProfileImageKey);

        s3ImageService.deleteProfileImage(oldProfileImageKey);

        String imageUrl = s3ImageService.createPresignedUrl(newProfileImageKey);

        return new ProfileImageUploadResponse(
                member.getId(),
                newProfileImageKey,
                imageUrl
        );
    }

    // 프로필 이미지 Presigned URL 조회
    @Transactional(readOnly = true)
    public ProfileImageResponse getProfileImage(Long memberId) {

        Member member = getMemberById(memberId);

        String profileImageKey = member.getProfileImageKey();

        if (profileImageKey == null || profileImageKey.isBlank()) {
            throw new ProfileImageNotFoundException();
        }

        String imageUrl = s3ImageService.createPresignedUrl(profileImageKey);

        return new ProfileImageResponse(
                member.getId(),
                profileImageKey,
                imageUrl
        );
    }

    private Member getMemberById(Long memberId) {
        return memberRepository.findById(memberId)
                .orElseThrow(MemberNotFoundException::new);
    }
}