package com.zerobase.orderapi.application;

import static com.zerobase.orderapi.exception.ErrorCode.*;

import com.zerobase.orderapi.client.UserClient;
import com.zerobase.orderapi.client.user.ChangeBalanceForm;
import com.zerobase.orderapi.client.user.CustomerDto;
import com.zerobase.orderapi.domain.model.ProductItem;
import com.zerobase.orderapi.domain.redis.Cart;
import com.zerobase.orderapi.exception.CustomException;
import com.zerobase.orderapi.service.ProductItemService;
import java.util.stream.IntStream;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Slf4j
@RequiredArgsConstructor
public class OrderApplication {
	private final CartApplication cartApplication;
	private final UserClient userClient;
	private final ProductItemService productItemService;

	@Transactional
	public void order(String token, Cart cart) {
		Cart orderCart = cartApplication.refreshCart(cart);

		if (!orderCart.getMessages().isEmpty()) {
			throw new CustomException(ORDER_FAIL_CHECK_CART);
		}

		CustomerDto customerDto = userClient.getCustomerInfo(token).getBody();

		Integer totalPrice = getTotalPrice(cart);
		if (customerDto.getBalance() < totalPrice) {
			throw new CustomException(ORDER_FAIL_CHECK_MONEY);
		}
		// 롤백 계획 생각해야함
		userClient.changeBalance(token,
			ChangeBalanceForm.builder()
				.from("USER")
				.message("Order")
				.money(-totalPrice)
				.build());

		for (Cart.Product product : orderCart.getProducts()) {
			for (Cart.ProductItem cartItem : product.getItems()) {
				ProductItem productItem = productItemService.getProductItem(cartItem.getId());
				productItem.setCount(productItem.getCount() - cartItem.getCount());

			}
		}
	}

	public Integer getTotalPrice(Cart cart) {
		return cart.getProducts().stream().flatMapToInt(product ->
				product.getItems().stream().flatMapToInt(productItem -> IntStream.of(
				productItem.getPrice() * productItem.getCount()))).sum();
	}
	// 필요한것
	// 1. 물건들이 전부 주문 가능한 상태 인지 확인
	// 2. 가격 변동이 있었는지에 대해 확인
	// 3. 고객의 돈이 충분한지
	// 4. 결제 & 상품의 재고 관리



}
