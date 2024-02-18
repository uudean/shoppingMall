package com.soloproject.shoppingmall.exception;

import lombok.Getter;

public enum ExceptionCode {
    MEMBER_EXISTS(404, "MEMBER EXISTS"),
    EMAIL_EXISTS(404,"EMAIL EXISTS"),
    MEMBER_NOT_FOUND(404,"MEMBER NOT FOUND"),
    PRODUCT_NOT_FOUND(404,"PRODUCT NOT FOUND"),
    CART_EMPTY(404,"CART IS EMPTY"),
    AUTH_CODE_DOES_NOT_MATCH(409,"AUTH_CODE_DOES_NOT MATCH"),
    UNAUTHORIZED(409,"UNAUTHORIZED");

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
