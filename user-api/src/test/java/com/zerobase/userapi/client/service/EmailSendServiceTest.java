package com.zerobase.userapi.client.service;

import com.zerobase.userapi.service.test.EmailSendService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class EmailSendServiceTest {

	@Autowired
	private EmailSendService emailSendService;

	@Test
	public void sendEmail () {
	    //given
		String response = emailSendService.sendEmail();
		System.out.println(response);
		//when
	    //then
	}
}