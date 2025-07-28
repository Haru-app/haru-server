package com.example.haruapp.global.service;

import com.example.haruapp.global.model.MailType;
import com.example.haruapp.global.util.EmailTemplateLoader;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.messaging.MessagingException;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
@RequiredArgsConstructor
public class MailService {

    private final JavaMailSender mailSender;
    private final EmailTemplateLoader templateLoader;

    public void sendMail(String to, MailType type, Map<String, Object> variables) throws MessagingException, jakarta.mail.MessagingException {
        String subject;
        String html;

        switch (type) {
            case TEMP_PASSWORD:
                subject = "[HaRU] 임시 비밀번호 안내";
                html = templateLoader.load("email-temp-password.html", variables);
                break;
            case SUBSCRIPTION_COMPLETE:
                subject = "[HaRU] 정기 구독 결제 완료 🎉";
                html = templateLoader.load("email-subscription-complete.html", variables);
                break;
            case SUBSCRIPTION_FAILED:
                subject = "[HaRU] 정기 구독 결제 실패 안내";
                html = templateLoader.load("email-subscription-failed.html", variables);
                break;
            default:
                throw new IllegalArgumentException("지원하지 않는 메일 타입");
        }

        MimeMessage message = mailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");
        helper.setFrom("your_email@gmail.com");
        helper.setTo(to);
        helper.setSubject(subject);
        helper.setText(html, true);
        mailSender.send(message);
    }

}
