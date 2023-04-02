package com.zerobase.orderapi.controller;

import com.zerobase.domain.config.JwtAuthenticationProvider;
import com.zerobase.orderapi.domain.product.AddProductForm;
import com.zerobase.orderapi.domain.product.AddProductItemForm;
import com.zerobase.orderapi.domain.product.ProductDto;
import com.zerobase.orderapi.domain.product.ProductItemDto;
import com.zerobase.orderapi.domain.product.UpdateProductForm;
import com.zerobase.orderapi.domain.product.UpdateProductItemFrom;
import com.zerobase.orderapi.service.ProductItemService;
import com.zerobase.orderapi.service.ProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/seller/product")
public class SellerProductController {

	private final ProductService productService;
	private final ProductItemService productItemService;
	private final JwtAuthenticationProvider provider;

	@PostMapping
	public ResponseEntity<ProductDto> addProduct(@RequestHeader(name = "X-AUTH-TOKEN") String token,
		@RequestBody AddProductForm form) {

		return ResponseEntity.ok(
			ProductDto.from(productService.addProduct(provider.getUserVo(token).getId(), form))
		);
	}

	@PostMapping("/item")
	public ResponseEntity<ProductDto> addProductItem(@RequestHeader(name = "X-AUTH-TOKEN") String token,
		@RequestBody AddProductItemForm form) {

		return ResponseEntity.ok(
			ProductDto.from(productItemService.addProductItem(provider.getUserVo(token).getId(), form))
		);
	}

	@PutMapping
	public ResponseEntity<ProductDto> updateProduct(@RequestHeader(name = "X-AUTH-TOKEN") String token,
		@RequestBody UpdateProductForm form) {

		return ResponseEntity.ok(
			ProductDto.from(productService.updateProduct(provider.getUserVo(token).getId(), form))
		);
	}

	@PutMapping("/item")
	public ResponseEntity<ProductItemDto> updateProductItem(@RequestHeader(name = "X-AUTH-TOKEN") String token,
		@RequestBody UpdateProductItemFrom form) {

		return ResponseEntity.ok(
			ProductItemDto.from(productItemService.updateProductItem(provider.getUserVo(token).getId(), form))
		);
	}
}
