package com.zerobase.orderapi.application;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

import com.zerobase.orderapi.domain.model.Product;
import com.zerobase.orderapi.domain.product.AddProductCartForm;
import com.zerobase.orderapi.domain.product.AddProductForm;
import com.zerobase.orderapi.domain.product.AddProductItemForm;
import com.zerobase.orderapi.domain.redis.Cart;
import com.zerobase.orderapi.domain.repository.ProductRepository;
import com.zerobase.orderapi.service.ProductService;
import java.util.ArrayList;
import java.util.List;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class CartApplicationTest {

	@Autowired
	private CartApplication cartApplication;
	@Autowired
	private ProductService productService;
	@Autowired
	private ProductRepository productRepository;

	@Test
	void ADD_TEST_MODIFY() {
	    //given
		Long customerId = 100L;

		cartApplication.clearCart(customerId);
		Product p = addProductTest();
		Product result = productRepository.findWithProductItemsById(p.getId()).get();

		//then
		assertNotNull(result);
		assertThat(result.getName()).isEqualTo("nike airForce");
		assertThat(result.getDescription()).isEqualTo("신발");
		assertThat(result.getProductItems().get(0).getName()).isEqualTo("nike airForce0");
		assertThat(result.getProductItems().get(1).getName()).isEqualTo("nike airForce1");
		assertThat(result.getProductItems().get(2).getName()).isEqualTo("nike airForce2");
	    //when
		Cart cart = cartApplication.addCart(customerId, makeAddForm(result));
		//then
		assertEquals(cart.getMessages().size(), 0);

		cart = cartApplication.getCart(customerId);
		assertEquals(cart.getMessages().size(), 1);

	}

	AddProductCartForm makeAddForm(Product p) {
		AddProductCartForm.ProductItem productItem =
			AddProductCartForm.ProductItem.builder()
				.id(p.getProductItems().get(0).getId())
				.name(p.getProductItems().get(0).getName())
				.count(5)
				.price(20000)
				.build();

		return 	AddProductCartForm.builder()
				.id(p.getId())
				.sellerId(p.getSellerId())
				.name(p.getName())
				.description(p.getDescription())
				.items(List.of(productItem))
				.build();
	}
	Product addProductTest () {
		//given
		Long sellerId = 1L;
		AddProductForm form = makeProductForm("nike airForce","신발", 3);

		//when
		return productService.addProduct(sellerId, form);
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
			.count(10)
			.build();
	}
}