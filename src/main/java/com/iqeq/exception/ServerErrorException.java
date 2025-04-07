package com.iqeq.exception;

import lombok.Getter;
import lombok.Setter;

import java.io.Serial;

@Getter
@Setter
public class ServerErrorException extends RuntimeException {
	@Serial
	private static final long serialVersionUID = 1L;

	public ServerErrorException(String message) {
		super(message);
	}
}
