package com.soloproject.shoppingmall.cart.service;

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

import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
@Transactional
@Service
public class CartService {

    private final CartRepository cartRepository;
    private final ProductRepository productRepository;
    private final MemberService memberService;
    private final CartProductRepository cartProductRepository;

    public Cart addProduct(CartPostDto cartPostDto) {

        Member loginMember = memberService.getLoginUser();
        // 장바구니 상품 존재 여부 확인
        boolean isExisted = cartRepository.findCartByMember_MemberId(loginMember.getMemberId()).isPresent();
        Product product = productRepository.findById(cartPostDto.getProductId()).orElseThrow();

//      장바구니가 비어 있을 경우
        if (!isExisted) {
            Cart newCart = new Cart();
            long totalPrice = calculateTotalPrice(cartPostDto);

            CartProduct newCartProduct = new CartProduct();
            newCartProduct.setProduct(product);
            newCartProduct.setQuantity(cartPostDto.getQuantity());
            newCartProduct.setCart(newCart);

            newCart.setMember(loginMember);
            newCart.setTotalPrice(totalPrice);

            cartProductRepository.save(newCartProduct);
            cartRepository.save(newCart);

            return newCart;
//            장바구니에 상품이 존재하는 경우
        } else {
            Cart cart = cartRepository.findCartByMember_MemberId(loginMember.getMemberId())
                    .orElseThrow(() -> new BusinessLogicException(ExceptionCode.CART_EMPTY));

            List<CartProduct> cartProducts = cart.getCartProducts();

            Optional<CartProduct> existProductInCartProduct = cartProducts.stream()
                    .filter(cartProduct -> cartProduct.getProduct().getProductId() == cartPostDto.getProductId())
                    .findFirst();

//            장바구니에 존재하는 상품을 장바구니에 담는경우
            if (existProductInCartProduct.isPresent()) {
                CartProduct cartProduct = existProductInCartProduct.get();
                long newQuantity = cartProduct.getQuantity() + cartPostDto.getQuantity();
                long newTotalPrice = calculateTotalPrice(cartPostDto) + cart.getTotalPrice();

                cartProduct.setQuantity(newQuantity);
                cartProductRepository.save(cartProduct);

                cart.setTotalPrice(newTotalPrice);
                cartRepository.save(cart);

                return cart;
//             새로운 상품을 장바구니에 담는 경우
            } else {
                long newTotalPrice = calculateTotalPrice(cartPostDto) + cart.getTotalPrice();

                CartProduct newCartProduct = new CartProduct();
                newCartProduct.setProduct(product);
                newCartProduct.setQuantity(cartPostDto.getQuantity());
                newCartProduct.setCart(cart);

                cart.setTotalPrice(newTotalPrice);

                cartProductRepository.save(newCartProduct);
                cartRepository.save(cart);

                return cart;
            }
        }
    }
//  회원 장바구니 조회
    public Cart getCart() {

        Member loginMember = memberService.getLoginUser();
        Cart cart = cartRepository.findCartByMember_MemberId(loginMember.getMemberId())
                .orElseThrow(() -> new BusinessLogicException(ExceptionCode.CART_EMPTY));

        return cart;
    }

//    장바구니 상품 수량 수정

//    장바구니 상품 삭제

    public long calculateTotalPrice(CartPostDto cartPostDto) {

        long quantity = cartPostDto.getQuantity();
        long productId = cartPostDto.getProductId();
        Product product = productRepository.findById(productId).orElseThrow();

        long totalPrice = quantity * product.getPrice();

        return totalPrice;
    }
}

