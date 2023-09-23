package com.zerobase.orderapi.service;

import com.zerobase.orderapi.domain.model.Product;
import com.zerobase.orderapi.domain.model.ProductItem;
import com.zerobase.orderapi.domain.product.AddProductForm;
import com.zerobase.orderapi.domain.product.UpdateProductForm;
import com.zerobase.orderapi.domain.product.UpdateProductItemFrom;
import com.zerobase.orderapi.domain.repository.ProductRepository;
import com.zerobase.orderapi.exception.CustomException;
import com.zerobase.orderapi.exception.ErrorCode;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class ProductService {

	private final ProductRepository productRepository;

	@Transactional
	public Product addProduct(Long sellerId, AddProductForm form) {
		return productRepository.save(Product.of(sellerId, form));
	}

	@Transactional
	public Product updateProduct(Long sellerId, UpdateProductForm form) {
		Product product = productRepository.findBySellerIdAndId(sellerId, form.getId())
				.orElseThrow(() -> new CustomException(ErrorCode.NOT_FOUND_PRODUCT));

		product.setName(form.getName());
		product.setDescription(form.getDescription());

		for(UpdateProductItemFrom itemFrom : form.getItems()) {
			ProductItem item = product.getProductItems().stream()
				.filter(pi -> pi.getId().equals(itemFrom.getId()))
				.findFirst().orElseThrow(() -> new CustomException(ErrorCode.NOT_FOUND_ITEM));

			item.setName(itemFrom.getName());
			item.setPrice(itemFrom.getPrice());
			item.setCount(itemFrom.getCount());
		}
		return product;
	}

	@Transactional
	public void deleteProduct(Long seller, Long productId) {
		Product product = productRepository.findBySellerIdAndId(seller, productId)
				.orElseThrow(() -> new CustomException(ErrorCode.NOT_FOUND_PRODUCT));

		productRepository.delete(product);
	}
}
