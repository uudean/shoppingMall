package com.soloproject.shoppingmall.member.controller;

import com.soloproject.shoppingmall.member.dto.MemberPostDto;
import com.soloproject.shoppingmall.member.dto.MemberResponseDto;
import com.soloproject.shoppingmall.member.entity.Member;
import com.soloproject.shoppingmall.member.mapper.MemberMapper;
import com.soloproject.shoppingmall.member.service.MemberService;
import com.soloproject.shoppingmall.response.ErrorResponse;
import com.soloproject.shoppingmall.response.SingleResponseDto;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@Validated
@RequiredArgsConstructor
@RequestMapping("/member")
@RestController
public class MemberController {

    private final MemberService memberService;
    private final MemberMapper mapper;

    @PostMapping("/create")
    public ResponseEntity createMember(@Valid @RequestBody MemberPostDto memberPostDto) {
        Member member = memberService.createMember(mapper.memberPostDtoToMember(memberPostDto));
        MemberResponseDto response = mapper.memberToMemberResponseDto(member);

        return new ResponseEntity<>(new SingleResponseDto<>(response),HttpStatus.OK);
    }
}
