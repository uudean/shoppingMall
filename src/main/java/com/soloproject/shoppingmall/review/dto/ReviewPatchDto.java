package com.soloproject.shoppingmall.review.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ReviewPatchDto {
    private long reviewId;
    private String content;
}
