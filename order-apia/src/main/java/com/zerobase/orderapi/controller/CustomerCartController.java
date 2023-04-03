package com.zerobase.orderapi.controller;

import com.zerobase.domain.config.JwtAuthenticationProvider;
import com.zerobase.orderapi.application.CartApplication;
import com.zerobase.orderapi.domain.product.AddProductCartForm;
import com.zerobase.orderapi.domain.redis.Cart;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/customer/cart")
public class CustomerCartController {

	// 임시
	private final CartApplication cartApplication;
	private final JwtAuthenticationProvider provider;

	@PostMapping
	public ResponseEntity<Cart> addCart(
		@RequestHeader(name = "X-AUTH-TOKEN") String token,
		@RequestBody AddProductCartForm form) {

		return ResponseEntity.ok(cartApplication.addCart(provider.getUserVo(token).getId(), form));
	}

}
