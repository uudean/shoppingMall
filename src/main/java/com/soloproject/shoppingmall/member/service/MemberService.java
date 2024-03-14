package com.soloproject.shoppingmall.member.service;

import com.soloproject.shoppingmall.exception.BusinessLogicException;
import com.soloproject.shoppingmall.exception.ExceptionCode;
import com.soloproject.shoppingmall.member.dto.PasswordUpdateDto;
import com.soloproject.shoppingmall.member.entity.Member;
import com.soloproject.shoppingmall.member.repository.MemberRepository;
import com.soloproject.shoppingmall.redis.RedisUtil;
import com.soloproject.shoppingmall.security.AuthUtils;
import com.soloproject.shoppingmall.security.CustomAuthorityUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Transactional
@RequiredArgsConstructor
@Service
public class MemberService {

    private final MemberRepository memberRepository;
    private final PasswordEncoder passwordEncoder;
    private final CustomAuthorityUtils authorityUtils;
    private final RedisUtil redisUtil;

    //    회원 가입
    public Member createMember(Member member) {

        verifyExistEmail(member.getEmail());

        String encryptedPassword = passwordEncoder.encode(member.getPassword());
        member.setPassword(encryptedPassword);

        return memberRepository.save(member);
    }

    //    회원 정보 수정
    public Member updateMember(Member member) {

        Member findMember = findVerifiedMember(member.getMemberId());

        Optional.ofNullable(member.getName()).ifPresent(findMember::setName);
        Optional.ofNullable(member.getPhone()).ifPresent(findMember::setPhone);
        Optional.ofNullable(member.getAddress()).ifPresent(findMember::setAddress);
        Optional.ofNullable(member.getModifiedAt()).ifPresent(findMember::setModifiedAt);
        Optional.ofNullable(member.getPassword()).ifPresent(password -> findMember.setPassword(passwordEncoder.encode(password)));
        return memberRepository.save(findMember);
    }

    // 비밀번호 변경
    public Member updatePassword(PasswordUpdateDto passwordUpdateDto) {

        Member findMember = findVerifiedMember(passwordUpdateDto.getMemberId());

        boolean confirm = passwordEncoder.matches(passwordUpdateDto.getCurrentPassword(), findMember.getPassword());

        if (confirm) {
            Optional.ofNullable(passwordUpdateDto.getNewPassword()).ifPresent(newPassword -> findMember.setPassword(passwordEncoder.encode(newPassword)));
            Optional.ofNullable(findMember.getModifiedAt()).ifPresent(findMember::setModifiedAt);
        }else throw new BusinessLogicException(ExceptionCode.UNAUTHORIZED);
        return memberRepository.save(findMember);
    }


    //  회원가입 이메일 인증
    public void emailAuth(String email, int authNum) {

        boolean emailAuth = emailAuthenticate(email, authNum);

        // 인증 성공 시 권한 부여
        if (emailAuth) {

            Member member = memberRepository.findByEmail(email).orElseThrow();
            List<String> roles = new ArrayList<>(authorityUtils.createRoles(member.getEmail()));
            member.setRoles(roles);
            memberRepository.save(member);

        } else throw new BusinessLogicException(ExceptionCode.AUTH_CODE_DOES_NOT_MATCH);
    }

    // 이메일 인증 번호 일치 하는지 확인
    public boolean emailAuthenticate(String email, int authNum) {

        int object = Integer.parseInt(redisUtil.get("AuthNumber : " + email).toString());

        if (object == authNum) {

            redisUtil.delete("AuthNumber : " + email);
            return true;

        } else return false;
    }

    // 회원 탈퇴
    @Transactional
    public void deleteMember(long memberId) {

        memberRepository.delete(findVerifiedMember(memberId));

    }

    // 존재하는 이메일인지 확인
    public void verifyExistEmail(String email) {

        Optional<Member> findMember = memberRepository.findByEmail(email);

        if (findMember.isPresent()) {

            throw new BusinessLogicException(ExceptionCode.EMAIL_EXISTS);

        }
    }

    //    비밀번호 찾기
    @Transactional
    public void findPassword(String email, int authNum, String newPassword) {

        boolean auth = emailAuthenticate(email, authNum);
        if (auth) {
            Member member = memberRepository.findByEmail(email).orElseThrow(() -> new BusinessLogicException(ExceptionCode.MEMBER_NOT_FOUND));
            String encodePassword = passwordEncoder.encode(newPassword);
            member.setPassword(encodePassword);
            memberRepository.save(member);
        } else throw new BusinessLogicException(ExceptionCode.AUTH_CODE_DOES_NOT_MATCH);
    }

    public Member findVerifiedMember(long memberId) {

        Optional<Member> optionalMember = memberRepository.findById(memberId);
        Member findMember = optionalMember.orElseThrow(() -> new BusinessLogicException(ExceptionCode.MEMBER_NOT_FOUND));
        return findMember;

    }

    public Member findMember(long memberId) {
        Member member = memberRepository.findById(memberId).orElseThrow(() -> new BusinessLogicException(ExceptionCode.MEMBER_NOT_FOUND));
        return member;
    }

    public Member getLoginUser() {
        return memberRepository.findByEmail(AuthUtils.getAuthUser()).orElseThrow(() -> new BusinessLogicException(ExceptionCode.MEMBER_NOT_FOUND));
    }
}
