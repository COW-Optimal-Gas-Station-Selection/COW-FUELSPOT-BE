package com.cow.fuelspot.domain.member.entity;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Builder;
import lombok.NoArgsConstructor;

// 회원 엔티티 (DB에 저장될 구조 정의)
@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "member")
public class Member {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "member_id")
    private Long id;

    @Column(nullable = false, unique = true, length = 100)
    private String email;

    @Column(nullable = false)
    private String password;

    @Column(nullable = false, length = 50)
    private String nickname;

    // Enum 타입을 문자열로 저장
    @Enumerated(EnumType.STRING)
    @Column(name = "fuel_type", length = 20)
    private FuelType fuelType;

    private Integer radius;

    // 생성자 대신 사용하는 Builder 패턴
    @Builder
    public Member(String email, String password, String nickname, FuelType fuelType, Integer radius) {
        this.email = email;
        this.password = password;
        this.nickname = nickname;
        this.fuelType = fuelType;
        this.radius = radius;
    }

    // 비즈니스 로직 (정보 수정)
    // Setter를 사용하면 사용한 의도를 파악하기 어렵고, 일관성을 유지하기 힘들기 때문에 사용
    public void updateInfo(String nickname, FuelType fuelType, Integer radius) {
        // null이 들어오면 수정하지 않고 기존 값 유지
        if (nickname != null) this.nickname = nickname;
        if (fuelType != null) this.fuelType = fuelType;
        if (radius != null) this.radius = radius;
    }
}
