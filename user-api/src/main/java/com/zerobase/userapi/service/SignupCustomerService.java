package com.zerobase.userapi.service;

import com.zerobase.userapi.domain.SignUpForm;
import com.zerobase.userapi.domain.model.Customer;
import com.zerobase.userapi.repository.CustomerRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class SignupCustomerService {
	private final CustomerRepository customerRepository;

	public Customer signUp(SignUpForm form) {
		return customerRepository.save(Customer.from(form));
	}

}
