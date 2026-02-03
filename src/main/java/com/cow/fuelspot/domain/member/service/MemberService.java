package com.cow.fuelspot.domain.member.service;

import com.cow.fuelspot.domain.member.dto.MemberSignupRequest;
import com.cow.fuelspot.domain.member.entity.Member;
import com.cow.fuelspot.domain.member.repository.MemberRepository;
import com.cow.fuelspot.domain.member.dto.MemberInfoResponse;
import com.cow.fuelspot.domain.member.dto.MemberUpdateRequest;
import com.cow.fuelspot.domain.member.dto.PasswordChangeRequest;
import com.cow.fuelspot.global.common.code.ErrorCode;
import com.cow.fuelspot.global.common.exception.CustomException;

import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


// 회원 서비스 계층
@Service
@RequiredArgsConstructor // 의존성 주입
@Transactional(readOnly = true)
public class MemberService {

    private final MemberRepository memberRepository;
    private final PasswordEncoder passwordEncoder;

    // 회원가입
    @Transactional
    public Long signup(MemberSignupRequest request) {
        if (memberRepository.existsByEmail(request.getEmail())) {
            throw new CustomException(ErrorCode.DUPLICATE_EMAIL);
        }
        
        if (!request.getPassword().equals(request.getCheckPassword())) {
            throw new CustomException(ErrorCode.PASSWORD_CONFIRM_MISMATCH);
        }

        String encodedPassword = passwordEncoder.encode(request.getPassword());
        
        Member member = request.toEntity(encodedPassword);
        Member savedMember = memberRepository.save(member);
        
        return savedMember.getId();
    }

    // 내 정보 조회
    public MemberInfoResponse getMyInfo(String email) {
        Member member = findMemberByEmail(email);
        return MemberInfoResponse.from(member);
    }

    // 내 정보 수정
    @Transactional
    public MemberInfoResponse updateMyInfo(String email, MemberUpdateRequest request) {
        Member member = findMemberByEmail(email);
        member.updateInfo(request.getNickname(), request.getFuelType(), request.getRadius());
        return MemberInfoResponse.from(member);
    }

    // 회원 탈퇴
    @Transactional
    public void deleteMyAccount(String email) {
        Member member = findMemberByEmail(email);
        memberRepository.delete(member);
    }

    // 비밀번호 변경
    @Transactional
    public void changePassword(String email, PasswordChangeRequest request) {
        Member member = findMemberByEmail(email);

        if (!passwordEncoder.matches(request.getCurrentPassword(), member.getPassword())) {
            throw new CustomException(ErrorCode.PASSWORD_MISMATCH);
        }

        if (request.getCurrentPassword().equals(request.getNewPassword())) {
            throw new CustomException(ErrorCode.SAME_AS_OLD_PASSWORD);
        }

        if (!request.getNewPassword().equals(request.getCheckNewPassword())) {
            throw new CustomException(ErrorCode.PASSWORD_CONFIRM_MISMATCH);
        }

        String encodeNewPassword = passwordEncoder.encode(request.getNewPassword());
        member.changePassword(encodeNewPassword);
    }

    // 이메일로 회원 찾기 (공통 메서드)
    private Member findMemberByEmail(String email) {
        return memberRepository.findByEmail(email)
                .orElseThrow(() -> new CustomException(ErrorCode.MEMBER_NOT_FOUND));
    }
}
