package com.soloproject.shoppingmall.cart.mapper;


import com.soloproject.shoppingmall.cart.dto.CartPostDto;
import com.soloproject.shoppingmall.cart.dto.CartProductResponseDto;
import com.soloproject.shoppingmall.cart.dto.CartResponseDto;
import com.soloproject.shoppingmall.cart.entity.Cart;
import com.soloproject.shoppingmall.cart.entity.CartProduct;
import com.soloproject.shoppingmall.member.entity.Member;
import com.soloproject.shoppingmall.product.entity.Product;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.ArrayList;
import java.util.List;


@Mapper(componentModel = "spring")
public interface CartMapper {

    @Mapping(source = "member.memberId", target = "memberId")
    CartResponseDto cartToCartResponseDto(Cart cart);

    @Mapping(source = "product.productId", target = "productId")
    @Mapping(source = "cartProduct.cartProductId", target = "cartProductId")
    CartProductResponseDto cartProductToCartProductResponseDto(CartProduct cartProduct);

    @Mapping(source = "product.productId", target = "productId")
    List<CartProductResponseDto> cartProductToCartProductResponseDtos(List<CartProduct> cartProducts);

}
