package com.soloproject.shoppingmall.email.service;

import com.soloproject.shoppingmall.redis.RedisUtil;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Random;

@RequiredArgsConstructor
@Transactional
@Service
public class MailService {

    private final JavaMailSender javaMailSender;
    private final RedisUtil redisUtil;
    private int authNum;

    @Value("${spring.mail.username}")
    private String setFromEmail;

    public void joinEmail(String email) throws Exception {
        createRandomNum();
        String setToEmail = email;
        String title = "회원 가입 인증 메일 입니다.";
        String content =
                "회원 가입을 환영 합니다." +
                        "<br><br>" +
                        "인증 번호는 " + authNum + " 입니다." +
                        "<br>";

        mailSend(setFromEmail, setToEmail, title, content);

    }

    public void mailSend(String setFromEmail, String setToEmail, String title, String content) throws Exception {
        MimeMessage message = javaMailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message, true, "utf-8");
        helper.setFrom(setFromEmail);
        helper.setTo(setToEmail);
        helper.setSubject(title);
        helper.setText(content, true);
        javaMailSender.send(message);
        redisUtil.set("AuthNumber : "+setToEmail,authNum,10);
    }

    public void createRandomNum() {
        Random random = new Random();
        StringBuilder randomNum = new StringBuilder();
        for (int i = 0; i < 6; i++) {
            randomNum.append(random.nextInt(10));
        }

        authNum = Integer.parseInt(randomNum.toString());
    }
}
