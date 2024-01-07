package com.soloproject.shoppingmall.cart.controller;

import com.soloproject.shoppingmall.cart.dto.CartPostDto;
import com.soloproject.shoppingmall.cart.dto.CartResponseDto;
import com.soloproject.shoppingmall.cart.entity.Cart;
import com.soloproject.shoppingmall.cart.mapper.CartMapper;
import com.soloproject.shoppingmall.cart.service.CartService;
import com.soloproject.shoppingmall.response.SingleResponseDto;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


@RequiredArgsConstructor
@RequestMapping("/cart")
@RestController
public class CartController {

    private final CartService cartService;
    private final CartMapper cartMapper;

    @PostMapping
    public ResponseEntity addCart(@RequestBody CartPostDto cartPostDto) {
        Cart cart = cartService.addProduct(cartPostDto);
        CartResponseDto response = cartMapper.cartToCartResponseDto(cart);

        return new ResponseEntity<>(new SingleResponseDto<>(response), HttpStatus.CREATED);
    }



    @GetMapping
    public ResponseEntity getCart() {
        Cart cart = cartService.getCart();
        CartResponseDto response = cartMapper.cartToCartResponseDto(cart);

        return new ResponseEntity<>(new SingleResponseDto<>(response), HttpStatus.OK);
    }
}
