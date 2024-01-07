package com.soloproject.shoppingmall.image.entity;

import com.soloproject.shoppingmall.audit.Auditable;
import com.soloproject.shoppingmall.product.entity.Product;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Getter
@Entity
public class Image extends Auditable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long imageId;

    @Column(nullable = false)
    private String imageName;

    @Column(nullable = false)
    private String originalName;

    @Column(nullable = false)
    private String url;

    @JoinColumn(name = "product_id")
    @ManyToOne
    private Product product;
}
