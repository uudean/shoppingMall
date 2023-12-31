package com.soloproject.shoppingmall.product.entity;

import com.soloproject.shoppingmall.audit.Auditable;
import com.soloproject.shoppingmall.cart.entity.CartProduct;
import com.soloproject.shoppingmall.image.entity.Image;
import com.soloproject.shoppingmall.order.entity.OrderProduct;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@NoArgsConstructor
@Setter
@Getter
@Entity
public class Product extends Auditable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long productId;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private String description;

    @Column(nullable = false)
    private long price;

    @Column(nullable = false)
    private long stock;

    @OneToMany(mappedBy = "product")
    private List<Image> images = new ArrayList<>();

    @OneToMany(mappedBy = "product")
    private List<OrderProduct> orderProducts = new ArrayList<>();

    @OneToMany(mappedBy = "product")
    private List<CartProduct> cartProducts = new ArrayList<>();

    public void setOrderProduct(OrderProduct orderProduct) {
        this.orderProducts.add(orderProduct);
        if (orderProduct.getProduct() != this) {
            orderProduct.setProduct(this);
        }
    }
}
