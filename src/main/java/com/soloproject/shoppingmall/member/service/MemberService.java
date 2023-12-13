package com.soloproject.shoppingmall.member.service;

import com.soloproject.shoppingmall.exception.BusinessLogicException;
import com.soloproject.shoppingmall.exception.ExceptionCode;
import com.soloproject.shoppingmall.member.entity.Member;
import com.soloproject.shoppingmall.member.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@RequiredArgsConstructor
@Service
public class MemberService {

    private final MemberRepository memberRepository;
    private final PasswordEncoder passwordEncoder;

    //    회원가입 메서드
    @Transactional
    public Member createMember(Member member) {

        verifyExistEmail(member.getEmail());

        String encryptedPassword = passwordEncoder.encode(member.getPassword());
        member.setPassword(encryptedPassword);

        return memberRepository.save(member);
    }

    //    회원 정보 수정
    @Transactional
    public Member updateMember(Member member) {

        Member findMember = findVerifiedMember(member.getMemberId());

        Optional.ofNullable(member.getUsername()).ifPresent(findMember::setUsername);
        Optional.ofNullable(member.getPassword()).ifPresent(password->findMember.setPassword(passwordEncoder.encode(password)));

        return memberRepository.save(findMember);
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
        Member findMember = optionalMember.orElseThrow(()-> new BusinessLogicException(ExceptionCode.MEMBER_NOT_EXIST));
        return findMember;
    }
}
