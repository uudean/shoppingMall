package com.soloproject.shoppingmall.member.repository;

import com.soloproject.shoppingmall.member.entity.Member;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MemberRepository extends JpaRepository<Member,Long> {

}
