package com.soloproject.shoppingmall.exception;

import lombok.Getter;

public enum ExceptionCode {
    MEMBER_EXISTS(409, "MEMBER EXISTS"),
    EMAIL_EXISTS(409,"EMAIL EXISTS"),
    MEMBER_NOT_EXIST(409,"MEMBER NOT EXISTS"),
    MEMBER_NOT_FOUND(409,"MEMBER NOT FOUND"),

    CART_EMPTY(404,"CART EMPTY")
    ;

    @Getter
    private int status;

    @Getter
    private String message;

    ExceptionCode(int status, String message) {
        this.status = status;
        this.message = message;
    }
}
