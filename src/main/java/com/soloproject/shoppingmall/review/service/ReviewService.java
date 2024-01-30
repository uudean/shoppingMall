package com.soloproject.shoppingmall.review.service;

import com.soloproject.shoppingmall.exception.BusinessLogicException;
import com.soloproject.shoppingmall.exception.ExceptionCode;
import com.soloproject.shoppingmall.member.entity.Member;
import com.soloproject.shoppingmall.member.service.MemberService;
import com.soloproject.shoppingmall.product.entity.Product;
import com.soloproject.shoppingmall.product.repository.ProductRepository;
import com.soloproject.shoppingmall.review.entity.Review;
import com.soloproject.shoppingmall.review.repository.ReviewRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Transactional
@Service
public class ReviewService {

    private final ReviewRepository reviewRepository;
    private final MemberService memberService;
    private final ProductRepository productRepository;

    public Review createReview(Review review, long productId) {
        Member loginMember = memberService.getLoginUser();
        Product product = productRepository.findById(productId).orElseThrow(() -> new BusinessLogicException(ExceptionCode.PRODUCT_NOT_FOUND));
        review.setMember(loginMember);
        review.setProduct(product);
        reviewRepository.save(review);
        return review;
    }

    @Transactional(readOnly = true)
    public Review getReview(long reviewId) {
        return reviewRepository.findById(reviewId).orElseThrow();
    }

    @Transactional(readOnly = true)
    public Page<Review> getReviews(int page, int size, long productId) {
        PageRequest pageRequest = PageRequest.of(page,size, Sort.by("createdAt").descending());

        return reviewRepository.findAllByProduct_ProductId(pageRequest,productId);

    }
}
