package com.soloproject.shoppingmall.cart.controller;

import com.soloproject.shoppingmall.cart.dto.CartPatchDto;
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
    public ResponseEntity addToCart(@RequestBody CartPostDto cartPostDto) {
        Cart cart = cartService.addToCart(cartPostDto);
        CartResponseDto response = cartMapper.cartToCartResponseDto(cart);

        return new ResponseEntity<>(new SingleResponseDto<>(response), HttpStatus.CREATED);
    }

    @PatchMapping
    public ResponseEntity updateQuantity(@RequestBody CartPatchDto cartPatchDto) {
        Cart cart = cartService.updateQuantity(cartPatchDto);
        CartResponseDto response = cartMapper.cartToCartResponseDto(cart);

        return new ResponseEntity<>(new SingleResponseDto<>(response), HttpStatus.OK);
    }

    @DeleteMapping("/{cartProduct-id}")
    public ResponseEntity deleteProduct(@PathVariable("cartProduct-id") long cartProductId) {
        cartService.deleteProduct(cartProductId);
        return new ResponseEntity<>("삭제되었습니다", HttpStatus.NO_CONTENT);
    }

    @GetMapping
    public ResponseEntity getCart() {
        Cart cart = cartService.getCart();
        CartResponseDto response = cartMapper.cartToCartResponseDto(cart);

        return new ResponseEntity<>(new SingleResponseDto<>(response), HttpStatus.OK);
    }
}
