package com.soloproject.shoppingmall.member.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class EmailAuthDto {
    String email;
    int authNumber;
}
