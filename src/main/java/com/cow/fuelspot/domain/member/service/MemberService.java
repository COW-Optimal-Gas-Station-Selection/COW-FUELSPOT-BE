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
// 회원의 비즈니스 로직 처리 (회원가입, 로그인)
@Service
@RequiredArgsConstructor // 의존성 주입
@Transactional(readOnly = true)
public class MemberService {

    // 생성자 주입
    private final MemberRepository memberRepository; // DB 접근 도구
    private final PasswordEncoder passwordEncoder; // 비밀번호 암호화 도구

    // 회원가입
    // 이메일 중복 체크, 비밀번호 암호화, DB 저장
    @Transactional
    public Long signup(MemberSignupRequest request) {
        if (memberRepository.existsByEmail(request.getEmail())) {
            throw new CustomException(ErrorCode.DUPLICATE_EMAIL);
        }

        String encodedPassword = passwordEncoder.encode(request.getPassword());
        Member member = request.toEntity(encodedPassword);

        // DB 저장 및 ID 반환
        Member savedMember = memberRepository.save(member);
        return savedMember.getId();
    }

    // 내 정보 조회
    public MemberInfoResponse getMyInfo(String email) {
        Member member = findMemberByEmail(email);
        return MemberInfoResponse.from(member);
    }

    // 내 정보 수정
    // @Transactional: 트랜잭션 종료 시 변경된 데이터를 감지하여 자동으로 DB 업데이트 (Dirty Checking: 변경 감지)
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

        // 현재 비밀번호 일치 여부 확인
        if (!passwordEncoder.matches(request.getCurrentPassword(), member.getPassword())) {
            throw new CustomException(ErrorCode.PASSWORD_MISMATCH);
        }

        // 새 비밀번호와 기존 비밀번호 일치 여부 확인
        if (request.getCurrentPassword().equals(request.getNewPassword())) {
            throw new CustomException(ErrorCode.SAME_AS_OLD_PASSWORD);
        }

        // 새 비밀번호 암호화 및 변경
        String encodeNewPassword = passwordEncoder.encode(request.getNewPassword());
        member.changePassword(encodeNewPassword);
    }

    // 이메일로 회원 찾기 (공통 메서드)
    private Member findMemberByEmail(String email) {
        return memberRepository.findByEmail(email)
                .orElseThrow(() -> new CustomException(ErrorCode.MEMBER_NOT_FOUND));
    }
}
