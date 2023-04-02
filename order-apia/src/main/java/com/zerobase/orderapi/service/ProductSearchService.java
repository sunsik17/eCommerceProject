package com.zerobase.orderapi.service;

import static com.zerobase.orderapi.exception.ErrorCode.NOT_FOUND_PRODUCT;

import com.zerobase.orderapi.domain.model.Product;
import com.zerobase.orderapi.domain.repository.ProductRepository;
import com.zerobase.orderapi.exception.CustomException;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ProductSearchService {
	private final ProductRepository productRepository;

	public List<Product> searchByName(String name) {
		return productRepository.searchByName(name);
	}
	public Product getByProductId(Long productId) {

		return productRepository.findWithProductItemsById(productId)
				.orElseThrow(() -> new CustomException(NOT_FOUND_PRODUCT));
	}
	public List<Product> getListByProductIds(List<Long> productIds) {

		return productRepository.findAllById(productIds);
	}

}
