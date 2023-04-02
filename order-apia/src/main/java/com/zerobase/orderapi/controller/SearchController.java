package com.zerobase.orderapi.controller;

import com.zerobase.orderapi.domain.product.ProductDto;
import com.zerobase.orderapi.service.ProductSearchService;
import java.util.List;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/search/product")
public class SearchController {

	private final ProductSearchService productSearchService;

	@GetMapping
	public ResponseEntity<List<ProductDto>> searchByName(@RequestParam String name) {
		return ResponseEntity.ok(
			productSearchService.searchByName(name).stream()
				.map(ProductDto::withoutItemsFrom).collect(Collectors.toList())
		);
	}

	@GetMapping("/detail}")
	public ResponseEntity<ProductDto> getDetail(@RequestParam Long productId) {
		return ResponseEntity.ok(
			ProductDto.from(productSearchService.getByProductId(productId))
		);
	}
}
