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

            CartProduct newCartProduct = new CartProduct();
            newCartProduct.setProduct(product);
            newCartProduct.setQuantity(cartPostDto.getQuantity());
            newCartProduct.setCart(newCart);
            cartProductRepository.save(newCartProduct);

            newCart.setMember(loginMember);

            int totalPrice = product.getPrice() * cartPostDto.getQuantity();

            newCart.setTotalPrice(totalPrice);

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
                int newQuantity = cartProduct.getQuantity() + cartPostDto.getQuantity();

                cartProduct.setQuantity(newQuantity);
                cartProductRepository.save(cartProduct);

                int newTotalPrice = calculateTotalPrice(cart.getCartId());

                cart.setTotalPrice(newTotalPrice);
                cartRepository.save(cart);

                return cart;
//             새로운 상품을 장바구니에 담는 경우
            } else {
                CartProduct newCartProduct = new CartProduct();
                newCartProduct.setProduct(product);
                newCartProduct.setQuantity(cartPostDto.getQuantity());
                newCartProduct.setCart(cart);
                cartProductRepository.save(newCartProduct);

                int newTotalPrice = calculateTotalPrice(cart.getCartId());

                cart.setTotalPrice(newTotalPrice);

                cartRepository.save(cart);

                return cart;
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
        Cart cart = cartRepository.findCartByMember_MemberId(loginMember.getMemberId())
                .orElseThrow(() -> new BusinessLogicException(ExceptionCode.CART_EMPTY));

        List<CartProduct> cartProducts = cart.getCartProducts();

        Optional<CartProduct> existProductInCartProduct = cartProducts.stream()
                .filter(cartProduct -> cartProduct.getProduct().getProductId() == cartPatchDto.getProductId())
                .findFirst();

        CartProduct cartProduct = existProductInCartProduct.get();

        cartProduct.setQuantity(cartPatchDto.getQuantity());

        int newTotalPrice = calculateTotalPrice(cart.getCartId());

        cart.setTotalPrice(newTotalPrice);

        cartProductRepository.save(cartProduct);
        cartRepository.save(cart);

        return cart;
    }

    //    장바구니 상품 삭제
    public void deleteProduct(long cartProductId) {

        CartProduct cartProduct = cartProductRepository.findById(cartProductId).orElseThrow();
        Cart cart = cartProduct.getCart();

        cartProductRepository.delete(cartProduct);

        int newTotalPrice = calculateTotalPrice(cart.getCartId());

        cart.setTotalPrice(newTotalPrice);
        cartRepository.save(cart);
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

