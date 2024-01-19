package com.soloproject.shoppingmall.like.controller;

import com.soloproject.shoppingmall.like.dto.LikePostDto;
import com.soloproject.shoppingmall.like.dto.LikeResponseDto;
import com.soloproject.shoppingmall.like.entity.Like;
import com.soloproject.shoppingmall.like.mapper.LikeMapper;
import com.soloproject.shoppingmall.like.service.LikeService;
import com.soloproject.shoppingmall.response.MultiResponseDto;
import com.soloproject.shoppingmall.response.SingleResponseDto;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequiredArgsConstructor
@RequestMapping("/like")
@RestController
public class LikeController {

    private final LikeMapper likeMapper;
    private final LikeService likeService;

    // 상품 좋아요 기능
    @PostMapping
    public ResponseEntity like(@RequestBody LikePostDto likePostDto) {

        Like like = likeService.like(likeMapper.likePostDtoToLike(likePostDto));
        LikeResponseDto response = likeMapper.likeToLikeResponseDto(like);

        return new ResponseEntity<>(new SingleResponseDto<>(response), HttpStatus.OK);
    }
    // 로그인한 사용자의 좋아요한 상품 리스트
    @GetMapping
    public ResponseEntity likeList(@RequestParam int page,
                                   @RequestParam int size) {
        Page<Like> likePage = likeService.likeList(page-1,size);

        List<Like> likes = likePage.getContent();
        List<LikeResponseDto> response = likeMapper.likesToLikeResponseDtos(likes);

        return new ResponseEntity<>(new MultiResponseDto<>(response,likePage),HttpStatus.OK);
    }
}
