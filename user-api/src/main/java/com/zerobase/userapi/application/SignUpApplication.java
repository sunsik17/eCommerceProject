package com.zerobase.userapi.application;

import com.zerobase.userapi.client.MailgunClient;
import com.zerobase.userapi.client.mailgun.SendMailForm;
import com.zerobase.userapi.domain.SignUpForm;
import com.zerobase.userapi.domain.model.Customer;
import com.zerobase.userapi.exception.CustomException;
import com.zerobase.userapi.exception.ErrorCode;
import com.zerobase.userapi.service.SignupCustomerService;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class SignUpApplication {

	private final MailgunClient mailgunClient;
	private final SignupCustomerService signupCustomerService;

	public void customerVerify(String email, String code) {
		signupCustomerService.verifyEmail(email, code);
	}

	public String customerSignUp(SignUpForm form) {
		if (signupCustomerService.isEmailExist(form.getEmail())) {
			throw new CustomException(ErrorCode.ALREADY_REGISTER_ACCOUNT);
		} else {
			Customer c = signupCustomerService.signUp(form);
			String code = getRandomCode();

			mailgunClient.sendEmail(SendMailForm.builder()
									.from("tester@sunsiktester.com")
									.to(form.getEmail())
									.subject("Verification Email!")
									.text(getVerificationEmailBody(form.getEmail(), form.getName(), code))
									.build());

			signupCustomerService.changeCustomerValidateEmail(c.getId(), code);
			return "회원 가입에 성공하였습니다.";
		}
	}

	private String getRandomCode() {
		return RandomStringUtils.random(10, true, true);
	}

	private String getVerificationEmailBody(String email, String name, String code) {
		return "Hello " + name + "! Please Click Link for verification\n\n"
			+ "http://localhost:8082/customer/signup/verify/customer?email="
			+ email
			+ "&code="
			+ code;
	}
}
