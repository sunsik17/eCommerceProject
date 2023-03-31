package com.zerobase.userapi.application;

import static com.zerobase.userapi.exception.ErrorCode.ALREADY_REGISTER_ACCOUNT;

import com.zerobase.userapi.client.MailgunClient;
import com.zerobase.userapi.client.mailgun.SendMailForm;
import com.zerobase.userapi.domain.SignUpForm;
import com.zerobase.userapi.domain.model.Customer;
import com.zerobase.userapi.domain.model.Seller;
import com.zerobase.userapi.exception.CustomException;
import com.zerobase.userapi.service.customer.SignupCustomerService;
import com.zerobase.userapi.service.seller.SignUpSellerService;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class SignUpApplication {

	private final MailgunClient mailgunClient;
	private final SignupCustomerService signupCustomerService;
	private final SignUpSellerService signUpSellerService;

	public void customerVerify(String email, String code) {
		signupCustomerService.verifyEmail(email, code);
	}

	public String customerSignUp(SignUpForm form) {
		if (signupCustomerService.isEmailExist(form.getEmail())) {
			throw new CustomException(ALREADY_REGISTER_ACCOUNT);
		} else {
			Customer customer = signupCustomerService.signUp(form);
			String code = getRandomCode();

			mailgunClient.sendEmail(SendMailForm.builder()
									.from("tester@sunsiktester.com")
									.to(form.getEmail())
									.subject("Verification Email!")
									.text(getVerificationEmailBody(customer.getEmail(), customer.getName(), "customer", code))
									.build());

			signupCustomerService.changeCustomerValidateEmail(customer.getId(), code);
			return "회원 가입에 성공하였습니다.";
		}
	}

	public void sellerVerify(String email, String code) {
		signUpSellerService.verifyEmail(email, code);
	}

	public String sellerSignUp(SignUpForm form) {
		if (signUpSellerService.isEmailExist(form.getEmail())) {
			throw new CustomException(ALREADY_REGISTER_ACCOUNT);
		} else {
			Seller seller = signUpSellerService.signUp(form);
			String code = getRandomCode();

			mailgunClient.sendEmail(SendMailForm.builder()
									.from("tester@sunsiktester.com")
									.to(form.getEmail())
									.subject("Verification Eamil")
									.text(getVerificationEmailBody(seller.getEmail(), seller.getName(), "seller", code))
									.build());

			signUpSellerService.changeSellerValidateEmail(seller.getId(), code);
			return "회원 가입에 성공하였습니다.";
		}
	}

	private String getRandomCode() {
		return RandomStringUtils.random(10, true, true);
	}

	private String getVerificationEmailBody(String email, String name, String type, String code) {
		return "Hello " + name + "! Please Click Link for verification\n\n"
			+ "http://localhost:8082/signup/" + type + "/verify?email="
			+ email
			+ "&code="
			+ code;
	}
}
