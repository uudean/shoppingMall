package com.soloproject.shoppingmall.email.controller;

import com.soloproject.shoppingmall.email.dto.MailDto;
import com.soloproject.shoppingmall.email.service.MailService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RequiredArgsConstructor
@RestController
public class MailController {

    private final MailService mailService;

    @PostMapping("/mail")
    public String mailCheck(@RequestBody MailDto mailDto) throws Exception {
        mailService.joinEmail(mailDto.getEmail());
        log.info("# 인증 번호 메일 전송 완료 : {}", mailDto.getEmail());
        return "인증 번호 메일 전송 완료";
    }

    @PostMapping("/findPassword")
    public String mail(@RequestBody MailDto mailDto) throws Exception{
        mailService.findPassword(mailDto.getEmail());
        log.info("# 인증 번호 메일 전송 완료 : {}", mailDto.getEmail());
        return "인증 번호 메일 전송 완료";
    }
}
