package com.zerobase.userapi.controller;

import static com.zerobase.userapi.exception.ErrorCode.NOT_FOUND_USER;

import com.zerobase.domain.config.JwtAuthenticationProvider;
import com.zerobase.domain.domain.common.UserVo;
import com.zerobase.userapi.domain.customer.CustomerDto;
import com.zerobase.userapi.domain.model.Customer;
import com.zerobase.userapi.exception.CustomException;
import com.zerobase.userapi.service.CustomerService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/customer")
public class CustomerController {

	private final JwtAuthenticationProvider provider;
	private final CustomerService customerService;

	@GetMapping("/getInfo")
	public ResponseEntity<CustomerDto> getInfo(@RequestHeader(name = "X-AUTH-TOKEN") String token) {

		UserVo userVo = provider.getUserVo(token);
		Customer customer = customerService.findByIdAndEmail(userVo.getId(), userVo.getEmail())
			.orElseThrow(
				() -> new CustomException(NOT_FOUND_USER));

		return ResponseEntity.ok(CustomerDto.from(customer));
	}

}
