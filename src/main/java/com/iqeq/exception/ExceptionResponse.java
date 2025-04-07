package com.iqeq.exception;

import com.iqeq.util.CommonConstants;
import lombok.Getter;
import lombok.Setter;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@Getter
@Setter
public class ExceptionResponse {

	private int code;
	private String status = CommonConstants.ERROR_MSG;
	private List<String> message;
	private Object[] parameters;

}