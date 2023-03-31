package com.zerobase.userapi.application;

import com.zerobase.domain.config.JwtAuthenticationProvider;
import com.zerobase.domain.domain.common.UserType;
import com.zerobase.userapi.domain.SignInForm;
import com.zerobase.userapi.domain.model.Customer;
import com.zerobase.userapi.domain.model.Seller;
import com.zerobase.userapi.exception.CustomException;
import com.zerobase.userapi.exception.ErrorCode;
import com.zerobase.userapi.service.customer.CustomerService;
import com.zerobase.userapi.service.seller.SellerService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class SignInApplication {

	private final CustomerService customerService;
	private final SellerService sellerService;
	private final JwtAuthenticationProvider provider;

	public String customerLogin(SignInForm form) {
		Customer customer = customerService.findValidCustomer(form.getEmail(), form.getPassword())
			.orElseThrow(() -> new CustomException(ErrorCode.LOGIN_CHECK_FAIL));

		return provider.createToken(customer.getEmail(), customer.getId(), UserType.CUSTOMER);
	}

	public String sellerLogin(SignInForm form) {
		Seller seller = sellerService.findValidSeller(form.getEmail(), form.getPassword())
			.orElseThrow(() -> new CustomException(ErrorCode.LOGIN_CHECK_FAIL));

		return provider.createToken(seller.getEmail(), seller.getId(), UserType.SELLER);
	}
}
