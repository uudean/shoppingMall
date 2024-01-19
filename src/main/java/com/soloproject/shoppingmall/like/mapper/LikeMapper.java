package com.soloproject.shoppingmall.like.mapper;

import com.soloproject.shoppingmall.like.dto.LikePostDto;
import com.soloproject.shoppingmall.like.dto.LikeResponseDto;
import com.soloproject.shoppingmall.like.entity.Like;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper(componentModel = "spring")
public interface LikeMapper {
    Like likePostDtoToLike(LikePostDto likePostDto);

    @Mapping(source ="member.memberId",target = "memberId")
    LikeResponseDto likeToLikeResponseDto(Like like);

    List<LikeResponseDto> likesToLikeResponseDtos(List<Like> likes);
}
