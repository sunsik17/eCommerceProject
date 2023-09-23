package com.zerobase.userapi.client.service;

import com.zerobase.userapi.client.MailgunClient;
import com.zerobase.userapi.client.mailgun.SendMailForm;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class EmailSendServiceTest {

	@Autowired
	private MailgunClient mailgunClient;
	@Test
	public void sendEmail () {
	    //given
		String result = mailgunClient.sendEmail(
			SendMailForm
				.builder()
				.from("sunsig21c@gmail.com")
				.to("sunsig21c@gmail.com")
				.subject("send email testing")
				.text("simple email send test")
				.build()).getBody();

		System.out.println(result);
	}
}