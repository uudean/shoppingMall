package com.soloproject.shoppingmall.member.controller;

import com.soloproject.shoppingmall.member.dto.MemberPatchDto;
import com.soloproject.shoppingmall.member.dto.MemberPostDto;
import com.soloproject.shoppingmall.member.dto.MemberResponseDto;
import com.soloproject.shoppingmall.member.entity.Member;
import com.soloproject.shoppingmall.member.mapper.MemberMapper;
import com.soloproject.shoppingmall.member.service.MemberService;
import com.soloproject.shoppingmall.response.SingleResponseDto;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@Slf4j
@Validated
@RequiredArgsConstructor
@RequestMapping("/member")
@RestController
public class MemberController {

    private final MemberService memberService;
    private final MemberMapper mapper;

    @PostMapping("/signup")
    public ResponseEntity createMember(@Valid @RequestBody MemberPostDto memberPostDto) {
        Member member = memberService.createMember(mapper.memberPostDtoToMember(memberPostDto));
        MemberResponseDto response = mapper.memberToMemberResponseDto(member);

        return new ResponseEntity<>(new SingleResponseDto<>(response), HttpStatus.CREATED);
    }

    @PatchMapping("/update/{member-id}")
    public ResponseEntity updateMember(@PathVariable("member-id") long memberId,
                                       @Valid @RequestBody MemberPatchDto memberPatchDto) {
        memberPatchDto.setMemberId(memberId);

        Member member = memberService.updateMember(mapper.memberPatchDtoToMember(memberPatchDto));
        MemberResponseDto response = mapper.memberToMemberResponseDto(member);

        return new ResponseEntity<>(new SingleResponseDto<>(response), HttpStatus.OK);
    }

    @GetMapping("/mypage/{member-id}")
    public ResponseEntity getMember(@Valid @PathVariable("member-id") long memberId) {
        Member member = memberService.findMember(memberId);
        MemberResponseDto response = mapper.memberToMemberResponseDto(member);

        return new ResponseEntity<>(new SingleResponseDto<>(response), HttpStatus.OK);
    }

    @DeleteMapping("/delete/{member-id}")
    public ResponseEntity deleteMember(@PathVariable("member-id") long memberId){
        memberService.deleteMember(memberId);
        return new ResponseEntity<>("회원 탈퇴 성공",HttpStatus.NO_CONTENT);
    }
}
