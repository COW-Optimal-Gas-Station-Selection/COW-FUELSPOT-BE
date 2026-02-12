package com.cow.fuelspot.global.config;

import com.cow.fuelspot.global.jwt.JwtAuthenticationFilter;
import com.cow.fuelspot.global.jwt.JwtTokenProvider;
import com.cow.fuelspot.global.jwt.handler.JwtAccessDeniedHandler;
import com.cow.fuelspot.global.jwt.handler.JwtAuthenticationEntryPoint;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.Arrays;
import java.util.List;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

// 보안 설정 총괄 클래스
// 스프링 시큐리티의 규칙 정의
@Configuration
@EnableWebSecurity // 보안 기능 활성화
@RequiredArgsConstructor
public class SecurityConfig {

    private final JwtTokenProvider jwtTokenProvider; // 필터에 넣어줄 토큰 검사 도구

    // 보안 필터 체인 설정
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http, JwtAuthenticationEntryPoint jwtAuthenticationEntryPoint, JwtAccessDeniedHandler jwtAccessDeniedHandler) throws Exception{
        http
                .cors(cors -> cors.configurationSource(corsConfigurationSource()))


                .csrf(AbstractHttpConfigurer::disable)

                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))

                .exceptionHandling(exception -> exception
                        .authenticationEntryPoint(jwtAuthenticationEntryPoint)
                        .accessDeniedHandler(jwtAccessDeniedHandler))

                // 출입 권한 설정
                .authorizeHttpRequests(auth -> auth

                        // 로그인(/api/auth/login)이나 토큰 재발급 등 인증 관련 요청은 무조건 허용
                        .requestMatchers("/api/auth/**").permitAll()
                        // "/api/members" 로 오는 요청(회원가입)은 무조건 허용
                        .requestMatchers(HttpMethod.POST, "/api/members").permitAll()
                        // 이메일 인증 및 비밀번호 찾기 API 허용
                        .requestMatchers("/api/auth/email/**", "/api/auth/password/**").permitAll()
                        .requestMatchers("/api/map/**").permitAll()

                        // 스웨거 관련 주소 2개를 프리패스로 설정
                        .requestMatchers("/swagger-ui/**", "/v3/api-docs/**").permitAll()
                        .requestMatchers("/api/gas-stations/**").permitAll()

                        .requestMatchers(HttpMethod.GET, "/api/favorites/count/**").permitAll()

                        // 그 외의 모든 요청은 인증된(로그인한) 사람만 통과
                        .anyRequest().authenticated()
                )

                // 커스텀 필터 등록
                .addFilterBefore(new JwtAuthenticationFilter(jwtTokenProvider), UsernamePasswordAuthenticationFilter.class);


        return http.build();
    }
    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();

        // 프론트엔드 주소 허용 (모두 허용)
        configuration.setAllowedOriginPatterns(List.of("*"));

        // HTTP 메서드 허용
        configuration.setAllowedMethods(Arrays.asList("HEAD", "GET", "POST", "PUT", "DELETE", "PATCH", "OPTIONS"));

        // 헤더 허용
        configuration.setAllowedHeaders(List.of("*"));

        // 인증 정보(토큰) 허용
        configuration.setAllowCredentials(true);

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);
        return source;
    }

    // 비밀번호 암호화 도구 등록 (회원가입/로그인 시 사용)
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder(); // 강력하고 대중적인 해시 암호화 도구
    }
}
