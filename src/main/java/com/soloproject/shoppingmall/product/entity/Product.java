package com.soloproject.shoppingmall.product.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.soloproject.shoppingmall.audit.Auditable;
import com.soloproject.shoppingmall.cart.entity.CartProduct;
import com.soloproject.shoppingmall.image.entity.Image;
import com.soloproject.shoppingmall.order.entity.OrderProduct;
import com.soloproject.shoppingmall.review.entity.Review;
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
    private int price;

    @Column(nullable = false)
    private int stock;

    @Column(nullable = false)
    private int totalSales;

    @Column(nullable = false)
    private long views;

    @Column(nullable = false)
    private long likeCount;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private Category category;

    @OneToMany(mappedBy = "product",cascade = {CascadeType.PERSIST,CascadeType.REMOVE})
    private List<Image> images = new ArrayList<>();

    @JsonIgnore
    @OneToMany(mappedBy = "product", fetch = FetchType.LAZY)
    private List<OrderProduct> orderProducts = new ArrayList<>();

    @JsonIgnore
    @OneToMany(mappedBy = "product", fetch = FetchType.LAZY)
    private List<CartProduct> cartProducts = new ArrayList<>();

    @OneToMany(mappedBy = "product", cascade = {CascadeType.PERSIST, CascadeType.REMOVE},fetch = FetchType.LAZY)
    private List<Review> reviews = new ArrayList<>();

    public void setOrderProduct(OrderProduct orderProduct) {
        this.orderProducts.add(orderProduct);
        if (orderProduct.getProduct() != this) {
            orderProduct.setProduct(this);
        }
    }

    @Getter
    public enum Category {
        OUTER("아우터"),
        TOP("상의"),
        BOTTOM("하의"),
        SHOES("신발"),
        ACC("액세서리");

        private String title;

        Category(String title) {
            this.title = title;
        }

    }
}
