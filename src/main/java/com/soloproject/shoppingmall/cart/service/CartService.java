package com.soloproject.shoppingmall.cart.service;

import com.soloproject.shoppingmall.cart.dto.CartPatchDto;
import com.soloproject.shoppingmall.cart.dto.CartPostDto;
import com.soloproject.shoppingmall.cart.entity.Cart;
import com.soloproject.shoppingmall.cart.entity.CartProduct;
import com.soloproject.shoppingmall.cart.repository.CartProductRepository;
import com.soloproject.shoppingmall.cart.repository.CartRepository;
import com.soloproject.shoppingmall.exception.BusinessLogicException;
import com.soloproject.shoppingmall.exception.ExceptionCode;
import com.soloproject.shoppingmall.member.entity.Member;
import com.soloproject.shoppingmall.member.service.MemberService;
import com.soloproject.shoppingmall.product.entity.Product;
import com.soloproject.shoppingmall.product.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@RequiredArgsConstructor
@Transactional
@Service
public class CartService {

    private final CartRepository cartRepository;
    private final ProductRepository productRepository;
    private final MemberService memberService;
    private final CartProductRepository cartProductRepository;

    @Transactional
    public Cart addToCart(CartPostDto cartPostDto) {

        Member loginMember = memberService.getLoginUser();

        boolean isExistedCart = cartRepository.findCartByMember_MemberId(loginMember.getMemberId()).isPresent();

        Product product = productRepository.findById(cartPostDto.getProductId()).orElseThrow(() ->
                new BusinessLogicException(ExceptionCode.PRODUCT_NOT_FOUND));
        // 장바구니가 비어있는 경우
        if (!isExistedCart) {

            Cart cart = new Cart();

            CartProduct cartProduct = new CartProduct();
            cartProduct.setProduct(product);
            cartProduct.setQuantity(cartPostDto.getQuantity());
            cartProduct.setCart(cart);

            cart.setCartProducts(Collections.singletonList(cartProduct));

            int totalPrice = product.getPrice() * cartProduct.getQuantity();
            cart.setMember(loginMember);
            cart.setTotalPrice(totalPrice);

            cartRepository.save(cart);

            return cart;
        } else {

            Cart existCart = loginMember.getCart();

            CartProduct existCartProduct = cartProductRepository.findCartProduct(existCart.getCartId(), cartPostDto.getProductId());
            // 장바구니에 상품이 존재하지만 다른 상품을 추가할 경우
            if (existCartProduct == null) {

                CartProduct newCartProduct = new CartProduct();
                newCartProduct.setQuantity(cartPostDto.getQuantity());
                newCartProduct.setProduct(product);
                newCartProduct.setCart(existCart);

                int totalPrice = calculateTotalPrice(existCart.getCartId());
                existCart.setTotalPrice(totalPrice + (product.getPrice() * cartPostDto.getQuantity()));

                List<CartProduct> cartProducts = new ArrayList<>(existCart.getCartProducts());
                cartProducts.add(newCartProduct);
                existCart.setCartProducts(cartProducts);

                cartRepository.save(existCart);

                return existCart;
                // 장바구니에 이미 추가된 상품을 추가한 경우
            } else {
                existCartProduct.setQuantity(existCartProduct.getQuantity() + cartPostDto.getQuantity());

                cartProductRepository.save(existCartProduct);

                int totalPrice = calculateTotalPrice(existCart.getCartId());
                existCart.setTotalPrice(totalPrice);

                cartRepository.save(existCart);

                return existCart;
            }

        }

    }

    //  회원 장바구니 조회
    @Transactional(readOnly = true)
    public Cart getCart() {

        Member loginMember = memberService.getLoginUser();
        Cart cart = cartRepository.findCartByMember_MemberId(loginMember.getMemberId())
                .orElseThrow(() -> new BusinessLogicException(ExceptionCode.CART_EMPTY));

        return cart;
    }

    //    장바구니 상품 수량 수정
    public Cart updateQuantity(CartPatchDto cartPatchDto) {

        Member loginMember = memberService.getLoginUser();

        Cart cart = loginMember.getCart();

        CartProduct findCartProduct = cartProductRepository.findCartProduct(cart.getCartId(), cartPatchDto.getProductId());

        findCartProduct.setQuantity(cartPatchDto.getQuantity());
        cartProductRepository.save(findCartProduct);

        int newTotalPrice = calculateTotalPrice(cart.getCartId());
        cart.setTotalPrice(newTotalPrice);
        cartRepository.save(cart);

        return cart;

    }

    //    장바구니 상품 삭제
    public void deleteProduct(long cartProductId) {

        Member loginMember = memberService.getLoginUser();
        Cart cart = loginMember.getCart();

        CartProduct cartProduct = cartProductRepository.findById(cartProductId).orElseThrow();
        cartProduct.setCart(null);
        cart.getCartProducts().remove(cartProduct);

        int totalPrice = calculateTotalPrice(cart.getCartId());
        cart.setTotalPrice(totalPrice);
        cartRepository.save(cart);

        cartProductRepository.delete(cartProduct);
    }

    public int calculateTotalPrice(long cartId) {

        List<CartProduct> cartProducts = cartProductRepository.findAllByCart_CartId(cartId);
        int totalPrice = 0;

        for (CartProduct cartProduct : cartProducts) {
            long productId = cartProduct.getProduct().getProductId();
            int quantity = cartProduct.getQuantity();

            int productPrice = productRepository.findById(productId).orElseThrow().getPrice();
            int price = productPrice * quantity;
            totalPrice = totalPrice + price;

        }
        return totalPrice;
    }
}

