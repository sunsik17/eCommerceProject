package com.zerobase.userapi.service;

import static com.zerobase.userapi.exception.ErrorCode.ALREADY_VERIFY;
import static com.zerobase.userapi.exception.ErrorCode.EXPIRE_CODE;
import static com.zerobase.userapi.exception.ErrorCode.NOT_FOUND_USER;
import static com.zerobase.userapi.exception.ErrorCode.WRONG_VERIFICATION;

import com.zerobase.userapi.domain.SignUpForm;
import com.zerobase.userapi.domain.model.Customer;
import com.zerobase.userapi.exception.CustomException;
import com.zerobase.userapi.repository.CustomerRepository;
import java.time.LocalDateTime;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class SignupCustomerService {

	private final CustomerRepository customerRepository;

	public Customer signUp(SignUpForm form) {
		return customerRepository.save(Customer.from(form));
	}

	public boolean isEmailExist(String email) {
		return customerRepository.findByEmail(email).isPresent();
	}

	@Transactional
	public void verifyEmail(String email, String code) {
		Customer customer = customerRepository.findByEmail(email)
			.orElseThrow(() -> new CustomException(NOT_FOUND_USER));

		if (customer.isVerify()){
			throw new CustomException(ALREADY_VERIFY);
		} else if (!customer.getVerificationCode().equals(code)) {
			throw new CustomException(WRONG_VERIFICATION);
		} else if (customer.getVerifyExpired().isBefore(LocalDateTime.now())) {
			throw new CustomException(EXPIRE_CODE);
		}

		customer.setVerify(true);
	}

	@Transactional
	public LocalDateTime changeCustomerValidateEmail(Long customerId, String verificationCode) {
		Optional<Customer> customerOptional = customerRepository.findById(customerId);

		if (customerOptional.isPresent()) {
			Customer customer = customerOptional.get();
			customer.setVerificationCode(verificationCode);
			customer.setVerifyExpired(LocalDateTime.now().plusDays(1));
			return customer.getVerifyExpired();
		}
		throw new CustomException(NOT_FOUND_USER);
	}
}