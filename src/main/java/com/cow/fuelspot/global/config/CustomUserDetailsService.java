package com.cow.fuelspot.global.config;

import com.cow.fuelspot.domain.member.entity.Member;
import com.cow.fuelspot.domain.member.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

// 사용자 정보 로드 서비스
// DB의 Member 정보를 스프링 시큐리티 표준 객체(UserDetails)로 변환하여 연결
@Service
@RequiredArgsConstructor
public class CustomUserDetailsService implements UserDetailsService {

    private final MemberRepository memberRepository;

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        return memberRepository.findByEmail(email)
                .map(this::createUserDetails) // Member 객체를 UserDetails로 변환
                .orElseThrow(() -> new UsernameNotFoundException("해당하는 유저를 찾을 수 없습니다."));
    }

    // DB의 Member -> 시큐리티의 User 객체로 변환
    private UserDetails createUserDetails(Member member) {
        return User.builder()
                .username(member.getEmail()) // ID
                .password(member.getPassword()) // PW
                .roles("USER") // 권한 (일반 유저)
                .build();

    }
}
