package com.iqeq.exception;

import lombok.Getter;
import lombok.Setter;

import java.io.Serial;

@Getter
@Setter
public class ClientErrorException extends RuntimeException{
	
	@Serial
	private static final long serialVersionID = 1L;
	
	public ClientErrorException(String message) {
		super(message);
	}

}
