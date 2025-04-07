package com.iqeq.exception;

import lombok.Getter;
import lombok.Setter;

import java.io.Serial;

@Getter
@Setter
public class TokenExpiredException extends RuntimeException {

	@Serial
	private static final long serialVersionUID = 1L;

	public TokenExpiredException(String msg) {
		super(msg);
	}
}