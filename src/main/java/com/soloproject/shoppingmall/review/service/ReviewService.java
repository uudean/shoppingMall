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

import java.time.LocalDateTime;
import java.util.Optional;

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

    public Review updateReview(Review review) {

        Member logingMember = memberService.getLoginUser();
        Review findReview = reviewRepository.findById(review.getReviewId()).orElseThrow();

        if (findReview.getMember().getMemberId() == logingMember.getMemberId()) {

            Optional.ofNullable(review.getContent()).ifPresent(findReview::setContent);
            Optional.ofNullable(review.getModifiedAt()).ifPresent(localDateTime -> findReview.setModifiedAt(LocalDateTime.now()));
            return reviewRepository.save(findReview);

        } else throw new BusinessLogicException(ExceptionCode.UNAUTHORIZED);
    }

    @Transactional(readOnly = true)
    public Review getReview(long reviewId) {
        return reviewRepository.findById(reviewId).orElseThrow();
    }

    @Transactional(readOnly = true)
    public Page<Review> getReviews(int page, int size, long productId) {
        PageRequest pageRequest = PageRequest.of(page, size, Sort.by("createdAt").descending());

        return reviewRepository.findAllByProduct_ProductId(pageRequest, productId);

    }

    @Transactional(readOnly = true)
    public Page<Review> getMyReviews(int page, int size) {

        Member loginMember = memberService.getLoginUser();
        long memberId = loginMember.getMemberId();

        PageRequest pageRequest = PageRequest.of(page, size, Sort.by("createdAt").descending());

        return reviewRepository.findAllByMember_MemberId(pageRequest, memberId);
    }

    public void deleteReview(long reviewId) {

        Member loginMember = memberService.getLoginUser();
        Review findReview = reviewRepository.findById(reviewId).orElseThrow();

        if (findReview.getMember() == loginMember) {
//            findReview.setProduct(null);
//            findReview.setMember(null);
            reviewRepository.delete(findReview);
        } else throw new BusinessLogicException(ExceptionCode.UNAUTHORIZED);

    }
}
