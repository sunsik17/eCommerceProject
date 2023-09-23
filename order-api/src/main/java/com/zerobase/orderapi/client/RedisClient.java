package com.zerobase.orderapi.client;

import static com.zerobase.orderapi.exception.ErrorCode.CART_CHANGE_FAIL;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.zerobase.orderapi.domain.redis.Cart;
import com.zerobase.orderapi.exception.CustomException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;

@Service
@RequiredArgsConstructor
@Slf4j
public class RedisClient {

	private final RedisTemplate<String, Object> redisTemplate;
	private static final ObjectMapper objectMapper = new ObjectMapper();

	public <T> T get(Long key, Class<T> classType) {
		return get(key.toString(), classType);
	}

	private  <T> T get(String key, Class<T> classType) {
		String redisValue = (String) redisTemplate.opsForValue().get(key);

		if (ObjectUtils.isEmpty(redisValue)) {
			return null;
		} else {
			try {
				return objectMapper.readValue(redisValue, classType);
			} catch (JsonProcessingException e) {
				log.error("Parsing error", e);
				return null;
			}
		}
	}

	public void put(Long key, Cart cart) {
		put(key.toString(), cart);
	}
	private void put(String key, Cart cart) {
		try {
			redisTemplate.opsForValue().set(key, objectMapper.writeValueAsString(cart));
		} catch (JsonProcessingException e) {
			throw new CustomException(CART_CHANGE_FAIL);
		}
	}
}
