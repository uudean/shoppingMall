package com.soloproject.shoppingmall.review.controller;

import com.soloproject.shoppingmall.response.MultiResponseDto;
import com.soloproject.shoppingmall.response.SingleResponseDto;
import com.soloproject.shoppingmall.review.dto.ReviewPatchDto;
import com.soloproject.shoppingmall.review.dto.ReviewPostDto;
import com.soloproject.shoppingmall.review.dto.ReviewResponseDto;
import com.soloproject.shoppingmall.review.entity.Review;
import com.soloproject.shoppingmall.review.mapper.ReviewMapper;
import com.soloproject.shoppingmall.review.service.ReviewService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequiredArgsConstructor
@RequestMapping("/review")
@RestController
public class ReviewController {
    private final ReviewService reviewService;
    private final ReviewMapper reviewMapper;

    @PostMapping("/{product-id}")
    public ResponseEntity createReview(@RequestBody ReviewPostDto reviewPostDto,
                                       @PathVariable("product-id") long productId) {
        Review review = reviewService.createReview(reviewMapper.reviewPostDtoToReview(reviewPostDto), productId);
        ReviewResponseDto response = reviewMapper.reviewToReviewResponseDto(review);

        return new ResponseEntity<>(new SingleResponseDto<>(response), HttpStatus.CREATED);
    }

    @PatchMapping("/{review-id}")
    public ResponseEntity updateReview(@RequestBody ReviewPatchDto reviewPatchDto,
                                       @PathVariable("review-id") long reviewId) {
        reviewPatchDto.setReviewId(reviewId);
        Review review = reviewService.updateReview(reviewMapper.reviewPatchDtoToReview(reviewPatchDto));
        ReviewResponseDto response = reviewMapper.reviewToReviewResponseDto(review);

        return new ResponseEntity<>(new SingleResponseDto<>(response), HttpStatus.OK);
    }

    @GetMapping("/{review-id}")
    public ResponseEntity getReview(@PathVariable("review-id") long reviewId) {

        Review review = reviewService.getReview(reviewId);
        ReviewResponseDto response = reviewMapper.reviewToReviewResponseDto(review);

        return new ResponseEntity<>(new SingleResponseDto<>(response), HttpStatus.OK);
    }

    @GetMapping("/all/{product-id}")
    public ResponseEntity getReviews(@PathVariable("product-id") long productId,
                                     @RequestParam int page,
                                     @RequestParam int size) {

        Page<Review> reviewPage = reviewService.getReviews(page - 1, size, productId);
        List<Review> reviews = reviewPage.getContent();
        List<ReviewResponseDto> response = reviewMapper.reviewsToReviewResponseDtos(reviews);

        return new ResponseEntity<>(new MultiResponseDto<>(response, reviewPage), HttpStatus.OK);
    }

    @GetMapping("/myReviews")
    public ResponseEntity getMyReviews(@RequestParam int page,
                                       @RequestParam int size) {
        Page<Review> reviewPage = reviewService.getMyReviews(page - 1, size);
        List<Review> reviews = reviewPage.getContent();
        List<ReviewResponseDto> response = reviewMapper.reviewsToReviewResponseDtos(reviews);

        return new ResponseEntity<>(new MultiResponseDto<>(response, reviewPage), HttpStatus.OK);

    }

    @DeleteMapping("/{review-id}")
    public ResponseEntity deleteReview(@PathVariable("review-id") long reviewId) {
        reviewService.deleteReview(reviewId);

        return new ResponseEntity<>("삭제 되었습니다.", HttpStatus.NO_CONTENT);
    }
}
