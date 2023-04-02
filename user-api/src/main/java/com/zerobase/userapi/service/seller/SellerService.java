package com.zerobase.userapi.service.seller;

import com.zerobase.userapi.domain.model.Seller;
import com.zerobase.userapi.domain.repository.SellerRepository;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class SellerService {

	private final SellerRepository sellerRepository;

	public Optional<Seller> findByIdAndEmail(Long id, String email) {
		return sellerRepository.findById(id)
			.stream().filter(seller -> seller.getEmail().equals(email))
			.findFirst();
	}

	public Optional<Seller> findValidSeller(String email, String password) {
		return sellerRepository.findByEmailAndPasswordAndVerifyIsTrue(email, password);
	}
}
