package com.soloproject.shoppingmall.like.entity;

import com.soloproject.shoppingmall.audit.Auditable;
import com.soloproject.shoppingmall.member.entity.Member;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@Setter
@Getter
@Entity(name = "Likes")
public class Like extends Auditable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long likeId;

    @Column
    private long productId;

    @ManyToOne
    @JoinColumn(name = "member_id")
    private Member member;
}
