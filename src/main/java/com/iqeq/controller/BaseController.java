package com.iqeq.controller;

import com.iqeq.dto.common.Response;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

public abstract class BaseController {

	// Utility method to build response
	protected ResponseEntity<Response> buildResponse(HttpStatus status, String message, Object data) {
		Response response = new Response();
		response.setCode(status.value());
		response.setMessage(message);
		response.setData(data);
		return new ResponseEntity<>(response, status);

	}
}