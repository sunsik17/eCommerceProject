package com.zerobase.userapi.config;

import feign.auth.BasicAuthRequestInterceptor;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class FeignConfig {
	@Qualifier(value = "mailgun")
	@Bean
	public BasicAuthRequestInterceptor basicAuthenticationInterceptor() {
		return new BasicAuthRequestInterceptor("api", "17160480aa5ccad5160c47590205d6e5-d51642fa-8fb7169d");
	}
}
