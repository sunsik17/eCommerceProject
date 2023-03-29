package com.zerobase.userapi.client;

import com.zerobase.userapi.client.mailgun.SendMailForm;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.cloud.openfeign.SpringQueryMap;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;

@FeignClient(name = "mailgun", url = "https://api.mailgun.net/v3/")
@Qualifier("mailgun")
public interface MailgunClient {

	@PostMapping("sandbox18a29e8dfb674f2a9efe4f518173e4d4.mailgun.org/messages")
	ResponseEntity<String> sendEmail(@SpringQueryMap SendMailForm form);
}
