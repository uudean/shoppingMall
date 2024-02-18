package com.soloproject.shoppingmall.review.dto;

import com.soloproject.shoppingmall.audit.Auditable;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class ReviewResponseDto extends Auditable {
    private long reviewId;
    private String name;
    private long productId;
    private String content;
}
