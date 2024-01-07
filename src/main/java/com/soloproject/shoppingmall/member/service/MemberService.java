package com.soloproject.shoppingmall.member.service;

import com.soloproject.shoppingmall.exception.BusinessLogicException;
import com.soloproject.shoppingmall.exception.ExceptionCode;
import com.soloproject.shoppingmall.member.entity.Member;
import com.soloproject.shoppingmall.member.repository.MemberRepository;
import com.soloproject.shoppingmall.security.AuthUtils;
import com.soloproject.shoppingmall.security.CustomAuthorityUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
@Service
public class MemberService {

    private final MemberRepository memberRepository;
    private final PasswordEncoder passwordEncoder;
    private final CustomAuthorityUtils authorityUtils;

    //    회원 가입
    @Transactional
    public Member createMember(Member member) {

        verifyExistEmail(member.getEmail());

        String encryptedPassword = passwordEncoder.encode(member.getPassword());
        member.setPassword(encryptedPassword);

        List<String> roles = authorityUtils.createRoles(member.getEmail());
        member.setRoles(roles);

        return memberRepository.save(member);
    }

    //    회원 정보 수정
    @Transactional
    public Member updateMember(Member member) {

        Member findMember = findVerifiedMember(member.getMemberId());

        Optional.ofNullable(member.getName()).ifPresent(findMember::setName);
        Optional.ofNullable(member.getPassword()).ifPresent(password -> findMember.setPassword(passwordEncoder.encode(password)));
        Optional.ofNullable(member.getPhone()).ifPresent(findMember::setPhone);
        Optional.ofNullable(member.getAddress()).ifPresent(findMember::setAddress);
        Optional.ofNullable(member.getModifiedAt()).ifPresent(findMember::setModifiedAt);

        return memberRepository.save(findMember);
    }
    // 회원 탈퇴
    @Transactional(readOnly = true)
    public Member findMember(long memberId){
        Member member = memberRepository.findById(memberId).orElseThrow(()->new BusinessLogicException(ExceptionCode.MEMBER_NOT_FOUND));
        return member;
    }

    @Transactional
    public void deleteMember(long memberId) {

        memberRepository.delete(findVerifiedMember(memberId));

    }

    // 존재하는 이메일인지 확인하는 메서드
    private void verifyExistEmail(String email) {

        Optional<Member> findMember = memberRepository.findByEmail(email);

        if (findMember.isPresent()) {

            throw new BusinessLogicException(ExceptionCode.EMAIL_EXISTS);

        }
    }

    public Member findVerifiedMember(long memberId) {

        Optional<Member> optionalMember = memberRepository.findById(memberId);
        Member findMember = optionalMember.orElseThrow(() -> new BusinessLogicException(ExceptionCode.MEMBER_NOT_EXIST));
        return findMember;

    }

    public Member getLoginUser(){
        return memberRepository.findByEmail(AuthUtils.getAuthUser().getName()).orElseThrow();
    }
}
