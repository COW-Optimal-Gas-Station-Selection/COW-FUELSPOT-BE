package com.cow.fuelspot.domain.auth.service;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.io.UnsupportedEncodingException;

@Service
@RequiredArgsConstructor
public class EmailService {

    // 이메일 전송 인터페이스
    private final JavaMailSender javaMailSender;

    @Value("${spring.mail.username}")
    private String senderEmail;

    // 비동기 처리
    @Async
    public void sendEmail(String toEmail, String subject, String text) {
        try {
            // 이메일 메세지 객체 생성
            MimeMessage message = javaMailSender.createMimeMessage();
            // 이메일 설정을 돕는 Helper 객체 생성 (멀티파트 데이터, 인코딩 설정)
            MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");

            helper.setFrom(senderEmail, "FuelSpot");

            helper.setTo(toEmail); // 받는 사람 이메일 설정
            helper.setSubject(subject); // 이메일 제목 설정
            helper.setText(text, true); // 이메일 본문 설정 (HTML 형식 사용)

            // 이메일 전송
            javaMailSender.send(message);

        } catch (MessagingException | UnsupportedEncodingException e) {
            throw new RuntimeException("이메일 전송 실패", e);
        }
    }
}
