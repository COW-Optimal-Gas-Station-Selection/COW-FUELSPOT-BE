package com.cow.fuelspot.domain.member.service;

import com.cow.fuelspot.domain.member.dto.MemberSignupRequest;
import com.cow.fuelspot.domain.member.entity.Member;
import com.cow.fuelspot.domain.member.repository.MemberRepository;
import com.cow.fuelspot.domain.member.dto.MemberInfoResponse;
import com.cow.fuelspot.domain.member.dto.MemberUpdateRequest;
import com.cow.fuelspot.domain.member.dto.PasswordChangeRequest;

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
    public void signup(MemberSignupRequest request) {
        // 이메일 중복 검사
        if (memberRepository.existsByEmail(request.getEmail())) {
            throw new IllegalArgumentException("이미 가입된 이메일입니다.");
        }

        // 비밀번호 암호화
        String encodedPassword = passwordEncoder.encode(request.getPassword());

        // DTO -> Entity 변환 (DTO 속 toEntity 메서드 사용)
        Member member = request.toEntity(encodedPassword);

        // DB 저장
        memberRepository.save(member);
    }

    // 내 정보 조회
    public MemberInfoResponse getMyInfo(String email) {
        // 이메일로 회원 조회
        Member member = memberRepository.findByEmail(email)
                .orElseThrow(() -> new IllegalArgumentException("해당하는 유저가 없습니다."));

        // 응답용 DTO 변환해서 반환
        return MemberInfoResponse.from(member);
    }

    // 내 정보 수정
    // @Transactional: 트랜잭션 종료 시 변경된 데이터를 감지하여 자동으로 DB 업데이트 (Dirty Checking: 변경 감지)
    @Transactional
    public MemberInfoResponse updateMyInfo(String email, MemberUpdateRequest request) {
        // 이메일로 회원 조회
        Member member = memberRepository.findByEmail(email)
                .orElseThrow(() -> new IllegalArgumentException("해당하는 유저가 없습니다"));

        // 정보 변경
        member.updateInfo(request.getNickname(), request.getFuelType(), request.getRadius());

        // 수정된 정보를 DTO로 변환해서 반환
        return MemberInfoResponse.from(member);
    }

    // 회원 탈퇴
    @Transactional
    public void deleteMyAccount(String email) {
        // 이메일로 회원 조회
        Member member = memberRepository.findByEmail(email)
                .orElseThrow(() -> new IllegalArgumentException("해당하는 유저가 없습니다"));

        // DB에서 삭제
        memberRepository.delete(member);
    }

    @Transactional
    public void changePassword(String email, PasswordChangeRequest request) {
        // 회원 정보 조회
        Member member = memberRepository.findByEmail(email)
                .orElseThrow(() -> new IllegalArgumentException("회원 정보가 없습니다."));

        // 현재 비밀번호 일치 여부 확인
        if (!passwordEncoder.matches(request.getCurrentPassword(), member.getPassword())) {
            throw new IllegalArgumentException("현재 비밀번호가 일치하지 않습니다");
        }

        // 새 비밀번호와 기존 비밀번호 일치 여부 확인
        if (request.getCurrentPassword().equals(request.getNewPassword())) {
            throw new IllegalArgumentException("새 비밀번호는 기존 비밀번호와 다르게 설정해야 합니다.");
        }

        // 새 비밀번호 암호화 및 변경
        String encodeNewPassword = passwordEncoder.encode(request.getNewPassword());
        member.changePassword(encodeNewPassword);
    }
}
