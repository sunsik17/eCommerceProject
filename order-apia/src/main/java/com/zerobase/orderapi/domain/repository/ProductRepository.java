package com.zerobase.orderapi.domain.repository;

import com.zerobase.orderapi.domain.model.Product;
import java.util.Optional;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.EntityGraph.EntityGraphType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ProductRepository extends JpaRepository<Product, Long>, ProductRepositoryCustom {
	@EntityGraph(attributePaths = "productItems", type = EntityGraphType.LOAD)
	Optional<Product> findBySellerIdAndId(Long sellerId, Long id);

	@EntityGraph(attributePaths = "productItems", type = EntityGraphType.LOAD)
	Optional<Product> findWithProductItemsById(Long id);
}
