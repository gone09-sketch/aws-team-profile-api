package com.cloudteamprofileapi.service;

import com.cloudteamprofileapi.dto.MemberCreateRequest;
import com.cloudteamprofileapi.dto.MemberPatchRequest;
import com.cloudteamprofileapi.dto.MemberPatchResponse;
import com.cloudteamprofileapi.dto.MemberResponse;
import com.cloudteamprofileapi.entity.Member;
import com.cloudteamprofileapi.exception.MemberNotFoundException;
import com.cloudteamprofileapi.repository.MemberRepository;
import lombok.Getter;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;

@Getter
@Service
public class MemberService {

    private final MemberRepository memberRepository;

    public MemberService(MemberRepository memberRepository) {
        this.memberRepository = memberRepository;
    }

    // 멤버 등록
    @Transactional
    public MemberResponse createMember(MemberCreateRequest memberCreateRequest) {

        Member newMember = new Member(
                memberCreateRequest.getName(),
                memberCreateRequest.getAge(),
                memberCreateRequest.getMbti()
        );

       Member savedMember = memberRepository.save(newMember);
       return MemberResponse.from(savedMember);
    }

    // 멤버 전체 조회
    @Transactional(readOnly = true)
    public List<MemberResponse> findAllmembers() {

        List<Member> allMembers = memberRepository.findAll();

        List<MemberResponse> memberResponseList = allMembers.stream()
                .map(MemberResponse::from)
                .toList();

        return memberResponseList;
    }

    // 멤버 부분 조회
    @Transactional(readOnly = true)
    public MemberResponse findOneMember(Long memberId) {

        Member member = memberRepository.findById(memberId)
                .orElseThrow(MemberNotFoundException::new);

        return MemberResponse.from(member);
    }

    // 멤버 정보 수정
    @Transactional
    public MemberPatchResponse updateMember(MemberPatchRequest memberPatchRequest, Long memberId) {

        Member member = memberRepository.findById(memberId)
                .orElseThrow(MemberNotFoundException::new);

        Member updatedMember = member.update(
                memberPatchRequest.getName(),
                memberPatchRequest.getAge(),
                memberPatchRequest.getMbti()
        );

        return MemberPatchResponse.from(updatedMember);
    }

    // 멤버 삭제
    @Transactional
    public void deleteMember(Long memberId) {

        Member member = memberRepository.findById(memberId)
                .orElseThrow(MemberNotFoundException::new);

        memberRepository.delete(member);
    }
}