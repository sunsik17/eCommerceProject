package com.zerobase.userapi.repository;

import com.zerobase.userapi.domain.model.Seller;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SellerRepository extends JpaRepository<Seller, Long> {

	Optional<Seller> findByEmail(String email);
	Optional<Seller> findByEmailAndPasswordAndVerifyIsTrue(String email, String password);
}
