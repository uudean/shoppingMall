package com.soloproject.shoppingmall.like.service;

import com.soloproject.shoppingmall.like.entity.Like;
import com.soloproject.shoppingmall.like.repository.LikeRepository;
import com.soloproject.shoppingmall.member.entity.Member;
import com.soloproject.shoppingmall.member.repository.MemberRepository;
import com.soloproject.shoppingmall.member.service.MemberService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Slf4j
@Transactional
@RequiredArgsConstructor
@Service
public class LikeService {

    private final MemberService memberService;
    private final MemberRepository memberRepository;
    private final LikeRepository likeRepository;

    // 좋아요 기능
    public Like like(Like like) {

        Member loginMember = memberService.getLoginUser();

        List<Like> likeList = loginMember.getLikes();

        for (int i = 0; i < likeList.size(); i++) {
            long productId = likeList.get(i).getProductId();
            if (productId == like.getProductId()) {
                likeRepository.delete(like);
            }
        }
        return like;
    }

    // 로그인한 사용자의 좋아요 한 상품 리스트
    public Page<Like> likeList(int page, int size) {

        Member loginMember = memberService.getLoginUser();
        long memberId = loginMember.getMemberId();

        PageRequest pageRequest = PageRequest.of(page, size, Sort.by("createdAt").descending());

        return likeRepository.findAllByMember_MemberId(memberId, pageRequest);
    }
}
