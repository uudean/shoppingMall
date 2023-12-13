package com.soloproject.shoppingmall.member.mapper;

import com.soloproject.shoppingmall.member.dto.MemberPostDto;
import com.soloproject.shoppingmall.member.dto.MemberResponseDto;
import com.soloproject.shoppingmall.member.entity.Member;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface MemberMapper {
    Member memberPostDtoToMember (MemberPostDto memberPostDto);

    MemberResponseDto memberToMemberResponseDto (Member member);
}
