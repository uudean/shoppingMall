package com.soloproject.shoppingmall.cart.entity;

import com.soloproject.shoppingmall.audit.Auditable;
import com.soloproject.shoppingmall.member.entity.Member;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
@Entity
public class Cart extends Auditable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long cartId;

    @Column(nullable = false)
    private int totalPrice;

    @OneToOne
    @JoinColumn(name = "member_id")
    private Member member;

    @OneToMany(mappedBy = "cart", cascade = CascadeType.PERSIST)
    private List<CartProduct> cartProducts = new ArrayList<>();

}
