package com.zerobase.orderapi.application;

import static com.zerobase.orderapi.exception.ErrorCode.ITEM_COUNT_NOT_ENOUGH;
import static com.zerobase.orderapi.exception.ErrorCode.NOT_FOUND_PRODUCT;

import com.zerobase.orderapi.domain.model.Product;
import com.zerobase.orderapi.domain.product.AddProductCartForm;
import com.zerobase.orderapi.domain.redis.Cart;
import com.zerobase.orderapi.domain.model.ProductItem;
import com.zerobase.orderapi.exception.CustomException;
import com.zerobase.orderapi.service.CartService;
import com.zerobase.orderapi.service.ProductSearchService;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CartApplication {
	private final CartService cartService;
	private final ProductSearchService productSearchService;

	public Cart addCart(Long customerId, AddProductCartForm form) {

		Product product = productSearchService.getByProductId(form.getId());
		if (product == null) {
			throw new CustomException(NOT_FOUND_PRODUCT);
		}
		Cart cart = cartService.getCart(customerId);

		if (!addAble(cart, product, form)) {
			throw new CustomException(ITEM_COUNT_NOT_ENOUGH);
		}
		return cartService.addCart(customerId, form);
	}

	public Cart updateCart(Long customerId, Cart cart) {
		cartService.putCart(customerId, cart);
		return getCart(customerId);
	}

	public Cart getCart(Long customerId) {
		Cart cart = refreshCart(cartService.getCart(customerId));
		cartService.putCart(cart.getCustomerId(), cart);
		Cart returnCart = new Cart();
		returnCart.setCustomerId(customerId);
		returnCart.setProducts(cart.getProducts());
		returnCart.setMessages(cart.getMessages());
		cart.setMessages(new ArrayList<>());

		cartService.putCart(customerId, cart);
		return returnCart;
	}

	protected Cart refreshCart(Cart cart) {
		// 상품이나 상품의 아이템의 정보, 수량, 가격이 변동 체크
		// 그에 맞는 알람
		// 상품의 수량, 가격을 우리가 임의로 변경한다.
		Map<Long, Product> productMap = productSearchService.getListByProductIds(
			cart.getProducts().stream().map(Cart.Product::getId).collect(Collectors.toList()))
			.stream()
			.collect(Collectors.toMap(Product::getId, product -> product));

		for (int i = 0; i < cart.getProducts().size(); i++){

			Cart.Product cartProduct = cart.getProducts().get(i);

			Product p = productMap.get(cartProduct.getId());
			String cartProductName = cartProduct.getName();
			if (p == null) {
				cart.getProducts().remove(cartProduct);
				i--;
				cart.addMessage(cartProductName + " 상품이 삭제 됐습니다.");
				continue;
			}

			Map<Long, ProductItem> productItemMap = p.getProductItems().stream()
				.collect(Collectors.toMap(ProductItem::getId, productItem -> productItem));

			List<String> tmpMessages = new ArrayList<>();
			// case 별로 옳바른 오류를 반환하는지 체크
			for (int j = 0; j < cartProduct.getItems().size(); j++) {
				Cart.ProductItem cartProductItem = cartProduct.getItems().get(j);
				ProductItem pi = productItemMap.get(cartProductItem.getId());
				String cartProductItemName = cartProductItem.getName();
				if (pi == null) {
					cartProduct.getItems().remove(cartProductItem);
					j--;
					tmpMessages.add(cartProductItemName + " 옵션이 삭제 됐습니다.");
					continue;
				}
				boolean isPriceChanged = false, isCountNotEnough = false;
				if (!cartProductItem.getPrice().equals(pi.getPrice())){
					isPriceChanged = true;
					cartProductItem.setPrice(pi.getPrice());
				}
				if (cartProductItem.getCount() > pi.getCount()) {
					isCountNotEnough = true;
					cartProductItem.setCount(pi.getCount());
				}
				if (isPriceChanged && isCountNotEnough) {
					tmpMessages.add(cartProductItemName + " 가격이 변동 됐습니다. 수량이 부족하여 구매 가능한 최대치로 변경 됐습니다.");
				} else if (isCountNotEnough) {
					tmpMessages.add(cartProductItemName + " 수량이 부족하여 구매 가능한 최대치로 변경 됐습니다.");
				} else if (isPriceChanged) {
					tmpMessages.add(cartProductItemName + " 가격이 변동 됐습니다.");
				}
			}
			if (cartProduct.getItems().size() == 0) {
				cart.getProducts().remove(cartProduct);
				i--;
				cart.addMessage(cartProductName + " 상품의 옵션이 모두 없어져 구매가 불가능 합니다.");
			} else if (!tmpMessages.isEmpty()) {
				StringBuilder builder = new StringBuilder();
				builder.append(cartProductName + " 상품의 변동 사항");
				for (String message : tmpMessages) {
					builder.append(message);
					builder.append(", ");
				}
				cart.addMessage(builder.toString());
			}
		}
		return cart;
	}
	private boolean addAble(Cart cart, Product product, AddProductCartForm form) {
		Cart.Product cartProduct =
			cart.getProducts().stream().filter(p -> p.getId().equals(form.getId()))
				.findFirst().orElse(Cart.Product.builder()
					.id(product.getId())
					.items(Collections.emptyList()).build());

		Map<Long, Integer> cartItemCountMap = cartProduct.getItems().stream()
			.collect(Collectors.toMap(Cart.ProductItem::getId, Cart.ProductItem::getCount));

		Map<Long, Integer> currentItemCountMap = product.getProductItems().stream()
			.collect(Collectors.toMap(ProductItem::getId, ProductItem::getCount));

		return 	form.getItems().stream().noneMatch(
			formItem -> {
				Integer cartCount = cartItemCountMap.get(formItem.getId());
				if (cartCount == null) {
					cartCount = 0;
				}
				Integer currentCount = currentItemCountMap.get(formItem.getId());
				return formItem.getCount() + cartCount > currentCount;
			});
	}

	public void clearCart(Long customerId) {
		cartService.putCart(customerId, null);
	}
}
