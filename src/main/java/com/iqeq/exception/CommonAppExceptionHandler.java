package com.iqeq.exception;

import com.iqeq.dto.common.Response;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.ConstraintViolationException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.NoHandlerFoundException;

import java.util.ArrayList;
import java.util.List;

@Slf4j
@RestControllerAdvice
public class CommonAppExceptionHandler {

	@ExceptionHandler(ResourceNotFoundException.class)
	public ResponseEntity<Response> resourceNotFoundExceptionHandler(ResourceNotFoundException e) {
	    log.error(e.getMessage(), e);
	    String message = e.getMessage();
	    Response rs = new Response();
	    rs.setStatus("Not Found");
	    rs.setCode(HttpStatus.NOT_FOUND.value());
	    rs.setMessage(message);
	    rs.setData(null);
	    return new ResponseEntity<>(rs, HttpStatus.NOT_FOUND);
	}

	/**
	 * Special exception.
	 *
	 * @param e the exception
	 * @return the response entity
	 */
	@ExceptionHandler(CustomException.class)
	public ResponseEntity<ExceptionResponse> specialException(CustomException e) {
	    log.error(e.getMessage(), e);
	    ExceptionResponse eR = new ExceptionResponse();
	    eR.setCode(HttpStatus.BAD_REQUEST.value());

	    List<String> errorMessages = new ArrayList<>();
	    errorMessages.add(e.getMessage());
	    eR.setMessage(errorMessages);
	    eR.setParameters(e.getParameters());

	    return new ResponseEntity<>(eR, HttpStatus.BAD_REQUEST);
	}

	@ExceptionHandler(TokenExpiredException.class)
	public ResponseEntity<Response> handleTokenExpiredException(TokenExpiredException e) {
	    String message = e.getMessage();
	    Response rs = new Response();
	    rs.setStatus(HttpStatus.UNAUTHORIZED.name());
	    rs.setCode(HttpStatus.UNAUTHORIZED.value());
	    rs.setMessage(message);
	    rs.setData(null);
	    return new ResponseEntity<>(rs, HttpStatus.UNAUTHORIZED);
	}

	@ExceptionHandler(AzureFileUploadException.class)
	public ResponseEntity<Response> handleAzureFileUploadException(AzureFileUploadException e) {
		String message = e.getMessage();
		Response rs = new Response();
		rs.setStatus(HttpStatus.INTERNAL_SERVER_ERROR.name());
		rs.setCode(HttpStatus.INTERNAL_SERVER_ERROR.value());
		rs.setMessage(message);
		rs.setData(null);
		return new ResponseEntity<>(rs, HttpStatus.INTERNAL_SERVER_ERROR);
	}

	@ExceptionHandler(NoHandlerFoundException.class)
	public ResponseEntity<Response> handleNotFound(NoHandlerFoundException e) {
	    String message = e.getMessage();
	    Response rs = new Response();
	    rs.setStatus(HttpStatus.NOT_FOUND.name());
	    rs.setCode(HttpStatus.NOT_FOUND.value());
	    rs.setMessage(message);
	    rs.setData(null);
	    return new ResponseEntity<>(rs, HttpStatus.NOT_FOUND);
	}

	@ExceptionHandler(ClientErrorException.class)
	public ResponseEntity<Response> handleClientErrorException(ClientErrorException e) {
	    String message = e.getMessage();
	    Response rs = new Response();
	    rs.setStatus(HttpStatus.BAD_REQUEST.name());
	    rs.setCode(HttpStatus.BAD_REQUEST.value());
	    rs.setMessage(message);
	    rs.setData(null);
	    return new ResponseEntity<>(rs, HttpStatus.BAD_REQUEST);
	}
	
	@ExceptionHandler(ServerErrorException.class)
	public ResponseEntity<Response> handleServerErrorException(ServerErrorException e) {
	    String message = e.getMessage();
	    Response rs = new Response();
	    rs.setStatus(HttpStatus.INTERNAL_SERVER_ERROR.name());
	    rs.setCode(HttpStatus.INTERNAL_SERVER_ERROR.value());
	    rs.setMessage(message);
	    rs.setData(null);
	    return new ResponseEntity<>(rs, HttpStatus.INTERNAL_SERVER_ERROR);
	}

	@ExceptionHandler(RuntimeException.class)
	public ResponseEntity<Response> handleRuntimeException(RuntimeException e) {
	    log.error(e.getMessage(), e);
	    String message = e.getMessage();
	    Response rs = new Response();
	    rs.setStatus(HttpStatus.INTERNAL_SERVER_ERROR.name());
	    rs.setCode(HttpStatus.INTERNAL_SERVER_ERROR.value());
	    rs.setMessage(message);
	    rs.setData(null);
	    return new ResponseEntity<>(rs, HttpStatus.INTERNAL_SERVER_ERROR);
	}

	@ExceptionHandler(MethodArgumentNotValidException.class)
	public ResponseEntity<ExceptionResponse> methodArgumentValidation(MethodArgumentNotValidException e) {
	    List<String> errorMessages = new ArrayList<>();

	    e.getBindingResult().getAllErrors().forEach(error -> {
	        String errorMessage = error.getDefaultMessage();
	        errorMessages.add(errorMessage);
	    });

	    ExceptionResponse eR = new ExceptionResponse();
	    eR.setCode(HttpStatus.BAD_REQUEST.value());
	    eR.setMessage(errorMessages);

	    return new ResponseEntity<>(eR, HttpStatus.BAD_REQUEST);
	}

	/**
	 * Special exception.
	 *
	 * @param e the e
	 * @return the response entity
	 */
	@ExceptionHandler(DataIntegrityViolationException.class)
	public ResponseEntity<ExceptionResponse> dataIntegrityViolationException(DataIntegrityViolationException e) {
	    ExceptionResponse eR = new ExceptionResponse();
	    eR.setCode(HttpStatus.BAD_REQUEST.value());

	    List<String> errorMessages = new ArrayList<>();
	    errorMessages.add(e.getMessage());
	    eR.setMessage(errorMessages);

	    return new ResponseEntity<>(eR, HttpStatus.BAD_REQUEST);
	}

	@ExceptionHandler({ConstraintViolationException.class})
	protected ResponseEntity<ExceptionResponse> handleConstraintViolationException(ConstraintViolationException e) {
	    ExceptionResponse eR = new ExceptionResponse();
	    eR.setCode(HttpStatus.BAD_REQUEST.value());

	    List<String> errorMessages = e.getConstraintViolations().stream()
	        .map(ConstraintViolation::getMessage)
	        .toList();
	    errorMessages.add(e.getMessage());
	    eR.setMessage(errorMessages);

	    return new ResponseEntity<>(eR, HttpStatus.BAD_REQUEST);
	}

	@ExceptionHandler(Exception.class)
	public ResponseEntity<Response> handleGenericException(Exception e) {
	    log.error(e.getMessage(), e);
	    String message = "An error occurred in application. Please contact support.";

	    Response rs = new Response();
	    rs.setStatus(HttpStatus.INTERNAL_SERVER_ERROR.name());
	    rs.setCode(HttpStatus.INTERNAL_SERVER_ERROR.value());
	    rs.setMessage(message);
	    rs.setData(null);

	    return new ResponseEntity<>(rs, HttpStatus.INTERNAL_SERVER_ERROR);
	}


}
