package com.zerobase.userapi.config;

import feign.auth.BasicAuthRequestInterceptor;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class FeignConfig {
	@Value("${mailgun.key}")
	private String MAILGUN_KEY;
	@Qualifier(value = "mailgun")
	@Bean
	public BasicAuthRequestInterceptor basicAuthenticationInterceptor() {
		return new BasicAuthRequestInterceptor("api", MAILGUN_KEY);
	}
}
