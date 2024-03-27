package com.soloproject.shoppingmall.main;

import com.soloproject.shoppingmall.email.dto.MailDto;
import com.soloproject.shoppingmall.member.dto.EmailAuthDto;
import com.soloproject.shoppingmall.member.dto.MemberPostDto;
import com.soloproject.shoppingmall.product.dto.ProductResponseDto;
import com.soloproject.shoppingmall.product.service.ProductService;
import com.soloproject.shoppingmall.security.LoginDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RequiredArgsConstructor
@Controller
public class ViewController {

    private final ProductService productService;

    @GetMapping("/main")
    public String main() {
        return "main";
    }

    @GetMapping("/signup")
    public String signup(Model model) {
        model.addAttribute("memberPostDto", new MemberPostDto());
        model.addAttribute("mailDto", new MailDto());
        return "signup";
    }

    @GetMapping("/loginPage")
    public String login(Model model) {
        model.addAttribute("loginDto", new LoginDto());
        return "loginPage";
    }

    @GetMapping("/emailAuth")
    public String mailAuth(Model model) {
        model.addAttribute("emailAuthDto", new EmailAuthDto.signup());
        return "emailAuth";
    }

    @GetMapping("/details/{product-id}")
    public String getProduct(Model model,
                             @PathVariable("product-id") long productId) {
        model.addAttribute("productId", productId);
        model.addAttribute("productResponseDto", new ProductResponseDto());
        productService.viewCount(productId);
        return "product";
    }

    @GetMapping("/productList")
    public String product(Model model,
                          @RequestParam int page,
                          @RequestParam int size,
                          @RequestParam String category,
                          @RequestParam int type) {

        model.addAttribute("page", page);
        model.addAttribute("size", size);
        model.addAttribute("category", category);
        model.addAttribute("type", type);
        model.addAttribute("productResponseDto", new ProductResponseDto());
        return "productList";
    }

    @GetMapping("/cartList")
    public String cartList() {
        return "cartList";
    }

    @GetMapping("/orderPage")
    public String orderPage() {
        return "orderPage";
    }

    @GetMapping("/payment")
    public String payment() {
        return "payment";
    }

    @GetMapping("/mypage/orderList")
    public String orderList() {
        return "orderList";
    }

    @GetMapping("/mypage/like")
    public String like() {
        return "like";
    }

    @GetMapping("/mypage/update")
    public String memberUpdate() {
        return "memberUpdate";
    }

    @GetMapping("/mypage/review")
    public String review() {
        return "writeReview";
    }
}
