package com.zerobase.orderapi.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import com.zerobase.orderapi.domain.model.Product;
import com.zerobase.orderapi.domain.product.AddProductForm;
import com.zerobase.orderapi.domain.product.AddProductItemForm;
import com.zerobase.orderapi.domain.repository.ProductRepository;
import java.util.ArrayList;
import java.util.List;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class ProductServiceTest {

	@Autowired
	private ProductService productService;
	@Autowired
	private ProductRepository productRepository;

	@Test
	void addProductTest () {
	    //given
		Long sellerId = 1L;
		AddProductForm form = makeProductForm("nike airForce","신발", 3);

	    //when
		Product p = productService.addProduct(sellerId, form);
		Product result = productRepository.findWithProductItemsById(p.getId()).get();

		//then
		assertNotNull(result);
		assertThat(result.getName()).isEqualTo("nike airForce");
		assertThat(result.getDescription()).isEqualTo("신발");
		assertThat(result.getProductItems().get(0).getName()).isEqualTo("nike airForce0");
		assertThat(result.getProductItems().get(1).getName()).isEqualTo("nike airForce1");
		assertThat(result.getProductItems().get(2).getName()).isEqualTo("nike airForce2");
	}

	private static AddProductForm makeProductForm(String name, String description, int itemCount) {
		List<AddProductItemForm> itemForms = new ArrayList<>();
		for (int i = 0; i < itemCount; i++) {
			itemForms.add(makeProductItemForm(null, name + i));
		}
		return AddProductForm.builder()
			.name(name)
			.description(description)
			.items(itemForms)
			.build();
	}

	private static AddProductItemForm makeProductItemForm(Long productId, String name){
		return AddProductItemForm.builder()
				.productId(productId)
				.name(name)
				.price(10000)
				.count(1)
				.build();
	}
}