package com.api.handler;

import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import com.api.error.ErrorDetails;
import com.api.error.ResourceNotFoundDetails;
import com.api.error.ResourceNotFoundDetails.Builder;
import com.api.error.ResourceNotFoundException;
import com.api.error.ValidationErrorDetails;
/***
 * Configures the exceptions in certain 
 * structures that will be received by the client.
 * @author Hugo
 *
 */
@ControllerAdvice// Will make global handlers
public class RestExceptionHandler extends ResponseEntityExceptionHandler{
	
	@ExceptionHandler(ResourceNotFoundException.class)
	public ResponseEntity<?> handleResourceNotFoundException(ResourceNotFoundException exception){
		ResourceNotFoundDetails rnfDetails = Builder.newBuilder()
			.timestamp(new Date().getTime())
			.status(HttpStatus.NOT_FOUND.value())
			.title("Resource not found")
			.detail(exception.getMessage())
			.developerMessager(exception.getClass().getName())
			.build();
		
		return new ResponseEntity<>(rnfDetails, HttpStatus.NOT_FOUND);
	}
	
	
	@Override //for handlers presents in class ResponseEntityExceptionHandler is not need to the annotation @ExceptionHadler
	public ResponseEntity<Object>  handleMethodArgumentNotValid(MethodArgumentNotValidException manvException,
			HttpHeaders headers, HttpStatus status, WebRequest request) {
		
		List<FieldError> fieldErrors = manvException.getBindingResult().getFieldErrors();
		String fields = fieldErrors.stream().map(FieldError::getField).collect(Collectors.joining(","));
		String fieldMessages = fieldErrors.stream().map(FieldError::getDefaultMessage).collect(Collectors.joining(","));
		
		ValidationErrorDetails vedDetails = ValidationErrorDetails.Builder.newBuilder()
			.timestamp(new Date().getTime())
			.status(HttpStatus.BAD_REQUEST.value())
			.title("Field Validation Error")
			.detail("Field Validation Error")
			.developerMessager(manvException.getClass().getName())
			.field(fields)
			.fieldMessage(fieldMessages)
			.build();
		
		return new ResponseEntity<>(vedDetails, HttpStatus.BAD_REQUEST);
	}
	/***
	 * receives all other exception handling present in the class ResponseEntityExceptionHandler
	 */
	@Override
	protected ResponseEntity<Object> handleExceptionInternal(Exception exception, Object body,
			HttpHeaders headers, HttpStatus status, WebRequest request) {

		ErrorDetails errorDetails = Builder.newBuilder()
				.timestamp(new Date().getTime())
				.status(status.value())
				.title("Internal Exception")
				.detail(exception.getMessage())
				.developerMessager(exception.getClass().getName())
				.build();
			
		return new ResponseEntity<>(errorDetails, HttpStatus.BAD_REQUEST);
	}
}
