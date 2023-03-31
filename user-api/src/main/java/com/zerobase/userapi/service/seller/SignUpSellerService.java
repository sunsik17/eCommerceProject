package com.zerobase.userapi.service.seller;

import static com.zerobase.userapi.exception.ErrorCode.ALREADY_VERIFY;
import static com.zerobase.userapi.exception.ErrorCode.EXPIRE_CODE;
import static com.zerobase.userapi.exception.ErrorCode.NOT_FOUND_USER;
import static com.zerobase.userapi.exception.ErrorCode.WRONG_VERIFICATION;

import com.zerobase.userapi.domain.SignUpForm;
import com.zerobase.userapi.domain.model.Seller;
import com.zerobase.userapi.exception.CustomException;
import com.zerobase.userapi.repository.SellerRepository;
import java.time.LocalDateTime;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class SignUpSellerService {

	private final SellerRepository sellerRepository;

	public Seller signUp(SignUpForm form) {
		return sellerRepository.save(Seller.from(form));
	}

	public boolean isEmailExist(String email) {
		return sellerRepository.findByEmail(email).isPresent();
	}

	@Transactional
	public void verifyEmail(String email, String code) {
		Seller seller = sellerRepository.findByEmail(email)
			.orElseThrow(() -> new CustomException(NOT_FOUND_USER));

		if (seller.isVerify()){
			throw new CustomException(ALREADY_VERIFY);
		} else if (!seller.getVerificationCode().equals(code)) {
			throw new CustomException(WRONG_VERIFICATION);
		} else if (seller.getVerifyExpired().isBefore(LocalDateTime.now())) {
			throw new CustomException(EXPIRE_CODE);
		}

		seller.setVerify(true);
	}
	@Transactional
	public LocalDateTime changeSellerValidateEmail(Long sellerId, String verificationCode) {
		Optional<Seller> sellerOptional = sellerRepository.findById(sellerId);

		if (sellerOptional.isPresent()) {
			Seller seller = sellerOptional.get();
			seller.setVerificationCode(verificationCode);
			seller.setVerifyExpired(LocalDateTime.now().plusDays(1));
			return seller.getVerifyExpired();
		}
		throw new CustomException(NOT_FOUND_USER);
	}
}
