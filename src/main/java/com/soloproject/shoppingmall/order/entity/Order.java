package com.soloproject.shoppingmall.order.entity;

import com.soloproject.shoppingmall.audit.Auditable;
import com.soloproject.shoppingmall.member.entity.Member;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@NoArgsConstructor
@Getter
@Setter
@Entity(name = "Orders")
public class Order extends Auditable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long orderId;

    @Column(nullable = false)
    private int totalPrice;

    @Column(nullable = false)
    private OrderStatus orderStatus;

    @ManyToOne
    @JoinColumn(name = "member_id")

    private Member member;

    @OneToMany(mappedBy = "order", cascade = CascadeType.PERSIST)
    private List<OrderProduct> orderProducts = new ArrayList<>();

    public void setMember(Member member) {
        this.member = member;
        if (!this.member.getOrders().contains(this)) {
            this.member.getOrders().add(this);
        }
    }

    public enum OrderStatus{
        ORDER_REQUEST(1,"결제 대기중"),
        ORDER_COMPLETE(2,"결제 완료");

        @Getter
        private int step;

        @Getter
        private String description;

        OrderStatus(int step, String description) {
            this.step = step;
            this.description = description;
        }
    }
}
