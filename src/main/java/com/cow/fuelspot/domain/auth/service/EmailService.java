package com.cow.fuelspot.domain.auth.service;

import com.cow.fuelspot.domain.auth.dto.EmailRequest;
import com.cow.fuelspot.domain.auth.entity.EmailVerification;
import com.cow.fuelspot.domain.auth.repository.EmailVerificationRepository;
import com.cow.fuelspot.domain.member.repository.MemberRepository;
import com.cow.fuelspot.global.common.code.ErrorCode;
import com.cow.fuelspot.global.common.exception.CustomException;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.UnsupportedEncodingException;
import java.security.SecureRandom;

@Slf4j
@Service
@RequiredArgsConstructor
public class EmailService {

    // 이메일 전송 인터페이스
    private final JavaMailSender javaMailSender;
    private final EmailVerificationRepository emailVerificationRepository;
    private final MemberRepository memberRepository;
    private static final SecureRandom secureRandom = new SecureRandom();

    @Value("${spring.mail.username}")
    private String senderEmail;

    public void sendVerificationCode(EmailRequest request) {
        if (!memberRepository.existsByEmail(request.getEmail())) {
            throw new CustomException(ErrorCode.MEMBER_NOT_FOUND);
        }

        String code = createRandomCode();

        EmailVerification verification = EmailVerification.builder()
                .email(request.getEmail())
                .code(code)
                .build();
        emailVerificationRepository.save(verification);

        sendEmail(request.getEmail(), "[FuelSpot] 인증 번호 안내",
                "<h3>인증 번호: " + code + "</h3><p>5분 안에 입력해주세요.</p>");
    }

    public void verifyCode(String email, String code) {
        EmailVerification verification = emailVerificationRepository.findById(email)
                .orElseThrow(() -> new CustomException(ErrorCode.VERIFICATION_CODE_NOT_FOUND));

        if (!verification.getCode().equals(code)) {
            throw new CustomException(ErrorCode.VERIFICATION_CODE_MISMATCH);
        }
    }

    public void deleteCode(String email) {
        emailVerificationRepository.deleteById(email);
    }

    @Async
    public void sendEmail(String toEmail, String subject, String text) {
        try {
            // 이메일 메세지 객체 생성
            MimeMessage message = javaMailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");

            helper.setFrom(senderEmail, "FuelSpot");
            helper.setTo(toEmail);
            helper.setSubject(subject);
            helper.setText(text, true);

            javaMailSender.send(message);
            log.info("Email sent successfully to: {}", toEmail);

        } catch (MessagingException | UnsupportedEncodingException e) {
            log.error("Email Send Failed: {}", e.getMessage());
        }
    }

    private String createRandomCode() {
        StringBuilder key = new StringBuilder();
        for (int i = 0; i < 6; i++) {
            key.append(secureRandom.nextInt(10));
        }
        return key.toString();
    }
}
