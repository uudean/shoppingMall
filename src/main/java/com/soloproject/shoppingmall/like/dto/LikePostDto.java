package com.soloproject.shoppingmall.like.dto;

import com.soloproject.shoppingmall.audit.Auditable;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class LikePostDto extends Auditable {
    private long memberId;
    private long productId;
}
