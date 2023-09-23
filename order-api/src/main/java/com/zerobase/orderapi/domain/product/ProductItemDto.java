package com.zerobase.orderapi.domain.product;

import com.zerobase.orderapi.domain.model.ProductItem;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class ProductItemDto {

	private Long id;
	private String name;
	private Integer price;
	private Integer count;

	public static ProductItemDto from(ProductItem item) {
		return ProductItemDto.builder()
			.id(item.getId())
			.name(item.getName())
			.count(item.getCount())
			.price(item.getPrice())
			.build();
	}
}
