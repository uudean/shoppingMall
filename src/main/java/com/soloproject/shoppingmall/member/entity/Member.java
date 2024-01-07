package com.soloproject.shoppingmall.member.entity;

import com.soloproject.shoppingmall.audit.Auditable;
import com.soloproject.shoppingmall.cart.entity.Cart;
import com.soloproject.shoppingmall.order.entity.Order;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@NoArgsConstructor
@Getter
@Setter
@Entity
public class Member extends Auditable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long memberId;

    @Column(nullable = false, unique = true)
    private String email;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private String password;

    @Column(nullable = false)
    private String address;

    @Column(nullable = false, unique = true)
    private String phone;

    @ElementCollection(fetch = FetchType.EAGER)
    private List<String> roles = new ArrayList<>();

    @OneToMany(mappedBy = "member")
    private List<Order> orders = new ArrayList<>();

    @OneToOne(mappedBy = "member",cascade = {CascadeType.PERSIST,CascadeType.REMOVE})
    private Cart cart;

    public enum MemberRole {
        ROLE_USER,
        ROLE_ADMIN
    }

}
