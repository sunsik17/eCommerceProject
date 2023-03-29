package com.zerobase.userapi.service;

import static org.assertj.core.api.Assertions.assertThat;

import com.zerobase.userapi.domain.SignUpForm;
import com.zerobase.userapi.domain.model.Customer;
import java.time.LocalDate;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

@SpringBootTest
@Transactional
class SignupCustomerServiceTest {

	@Autowired
	private SignupCustomerService signupCustomerService;
	@Test
	void signUp() {
	    //given
		SignUpForm form = SignUpForm.builder()
			.name("name")
			.phone("01012345678")
			.email("ss@ss.com")
			.birth(LocalDate.now())
			.password("1")
			.build();
		//when
		Customer customer = signupCustomerService.signUp(form);
		//then
		assertThat(customer).isNotNull();
		assertThat(customer.getName()).isEqualTo("name");
		assertThat(customer.getPhone()).isEqualTo("01012345678");
		assertThat(customer.getEmail()).isEqualTo("ss@ss.com");
	}
}