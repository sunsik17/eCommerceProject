package com.zerobase.userapi.domain.customer;

import lombok.Getter;

@Getter
public class ChangeBalanceForm {
	private String fromm;
	private String message;
	private Integer money;
}
