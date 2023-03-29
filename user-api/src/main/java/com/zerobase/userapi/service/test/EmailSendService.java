package com.zerobase.userapi.service.test;

import com.zerobase.userapi.client.MailgunClient;
import com.zerobase.userapi.client.mailgun.SendMailForm;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class EmailSendService {

	private final MailgunClient mailgunClient;

	public String sendEmail() {
		SendMailForm form = SendMailForm.builder()
			.from("sunsig21c@gmail.com")
			.to("sunsig21c@gmail.com")
			.subject("Test Email")
			.text("my text")
			.build();

		return mailgunClient.sendEmail(form).getBody();
	}
}
