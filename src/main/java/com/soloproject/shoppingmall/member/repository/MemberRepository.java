package com.soloproject.shoppingmall.member.repository;

import com.soloproject.shoppingmall.member.entity.Member;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface MemberRepository extends JpaRepository<Member, Long> {
    Optional<Member> findByEmail(String email);

    @Query("SELECT COUNT(m) FROM Member m where m.email = :email")
    Long existEmail(String email);
}
