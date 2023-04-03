package com.zerobase.orderapi.exception;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@RequiredArgsConstructor
@Getter
public enum ErrorCode {
	SAME_ITEM_NAME(HttpStatus.BAD_REQUEST, "아이템 이름이 중복입니다."),
	NOT_FOUND_ITEM(HttpStatus.BAD_REQUEST, "아이템을 찾을 수 없습니다."),
	CART_CHANGE_FAIL(HttpStatus.BAD_REQUEST, "장바구니를 추가 할 수 없습니다."),
	ITEM_COUNT_NOT_ENOUGH(HttpStatus.BAD_REQUEST, "상품의 수량이 부족합니다."),
	NOT_FOUND_PRODUCT(HttpStatus.BAD_REQUEST, "상품을 찾을 수 없습니다.");
	private final HttpStatus httpStatus;
	private final String detail;
}
