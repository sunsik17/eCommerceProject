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
	ORDER_FAIL_CHECK_CART(HttpStatus.BAD_REQUEST, "주문 불가! 장바구니를 확인 해 주세요."),
	ORDER_FAIL_CHECK_MONEY(HttpStatus.BAD_REQUEST, "주문 불가! 잔액이 부족합니다. 잔액을 확인 해 주세요"),
	NOT_FOUND_PRODUCT(HttpStatus.BAD_REQUEST, "상품을 찾을 수 없습니다.");
	private final HttpStatus httpStatus;
	private final String detail;
}
