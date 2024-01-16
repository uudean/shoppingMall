package com.soloproject.shoppingmall.order.entity;

import com.soloproject.shoppingmall.audit.Auditable;
import com.soloproject.shoppingmall.product.entity.Product;
import jakarta.persistence.*;
import jakarta.validation.constraints.Min;
import lombok.Getter;
import lombok.Setter;


@Getter
@Setter
@Entity
public class OrderProduct extends Auditable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long orderProductId;

    @Min(value = 1, message = "수량은 최소 1개 이상이어야 합니다.")
    private int quantity;

    @ManyToOne
    @JoinColumn(name = "order_id")
    private Order order;

    @ManyToOne
    @JoinColumn(name = "product_id")
    private Product product;

    public void setProduct(Product product){
        this.product=product;
        if (!this.product.getOrderProducts().contains(this)){
            this.product.setOrderProduct(this);
        }
    }
}
