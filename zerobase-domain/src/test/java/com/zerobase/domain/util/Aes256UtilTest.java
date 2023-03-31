package com.zerobase.domain.util;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;


class Aes256UtilTest {

	@Test
	void encryptAndDecrypt() {
		String encrypt = Aes256Util.encrypt("Hello world");
		assertEquals("Hello world", Aes256Util.decrypt(encrypt));
	}
}