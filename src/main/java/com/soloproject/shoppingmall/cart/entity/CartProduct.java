package com.soloproject.shoppingmall.cart.entity;

import com.soloproject.shoppingmall.audit.Auditable;
import com.soloproject.shoppingmall.product.entity.Product;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
@Entity
public class CartProduct extends Auditable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long cartProductId;

    @Column(nullable = false)
    private int quantity;

    @ManyToOne
    @JoinColumn(name = "product_id")
    private Product product;

    @ManyToOne
    @JoinColumn(name = "cart_id")
    private Cart cart;

    public void setCart(Cart cart) {
        this.cart = cart;
        if (!this.cart.getCartProducts().contains(this)) {
            this.cart.getCartProducts().add(this);
        }
    }

    public void setProduct(Product product) {
        this.product = product;
        if (!this.product.getCartProducts().contains(this)) {
            this.product.getCartProducts().add(this);
        }
    }
}
