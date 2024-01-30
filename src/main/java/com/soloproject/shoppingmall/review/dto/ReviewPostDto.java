package com.soloproject.shoppingmall.review.dto;

import com.soloproject.shoppingmall.audit.Auditable;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ReviewPostDto extends Auditable {
    private String content;
}
