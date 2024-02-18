package com.soloproject.shoppingmall.review.entity;

import com.soloproject.shoppingmall.audit.Auditable;
import com.soloproject.shoppingmall.member.entity.Member;
import com.soloproject.shoppingmall.product.entity.Product;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@Getter
@Setter
@Entity(name = "Reviews")
public class Review extends Auditable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long reviewId;

    @Column(nullable = false)
    private String content;

    @ManyToOne
    @JoinColumn(name = "member_id")
    private Member member;

    @ManyToOne
    @JoinColumn(name = "product_id")
    private Product product;
}
