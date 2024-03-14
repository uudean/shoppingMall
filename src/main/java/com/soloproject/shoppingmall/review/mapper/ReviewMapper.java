package com.soloproject.shoppingmall.review.mapper;

import com.soloproject.shoppingmall.product.dto.ProductResponseDto;
import com.soloproject.shoppingmall.review.dto.ReviewPatchDto;
import com.soloproject.shoppingmall.review.dto.ReviewPostDto;
import com.soloproject.shoppingmall.review.dto.ReviewResponseDto;
import com.soloproject.shoppingmall.review.entity.Review;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper(componentModel = "spring")
public interface ReviewMapper {
    Review reviewPostDtoToReview(ReviewPostDto reviewPostDto);

    Review reviewPatchDtoToReview(ReviewPatchDto reviewPatchDto);

    @Mapping(source = "member.name", target = "name")
    @Mapping(source = "product.productId", target = "productId")
    ReviewResponseDto reviewToReviewResponseDto(Review review);


    List<ReviewResponseDto> reviewsToReviewResponseDtos(List<Review> reviews);
}
