package com.zerobase.orderapi.domain.repository;

import com.zerobase.orderapi.domain.model.Product;
import java.util.List;

public interface ProductRepositoryCustom {

	List<Product> searchByName(String name);

}
