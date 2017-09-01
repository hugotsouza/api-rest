package com.api.error;

/***
 * Class created to be manipulated by class 
 * RestExceptionHandler with default structure for an exception of type 
 * MethodArgumentNotValidException sent to the client.
 * @author Hugo Trindade
 *
 */
public class ValidationErrorDetails extends ErrorDetails{
	
	private String field;
	private String fieldMessage;
	

	public String getField() {
		return field;
	}

	public String getFieldMessage() {
		return fieldMessage;
	}

	public ValidationErrorDetails(String title, int status, String detail, long timestamp, String developerMessage,
			String field, String fieldMessage) {
		this.title = title;
		this.status = status;
		this.detail = detail;
		this.timestamp = timestamp;
		this.developerMessage = developerMessage;
		this.field = field;
		this.fieldMessage = fieldMessage;
	}
	
	public static final class Builder{
		private String title;
		private int status;
		private String detail;
		private long timestamp;
		private String developerMessage;
		private String field;
		private String fieldMessage;
		
		private Builder(){}
		
		public static Builder newBuilder(){
			return new Builder();
		}
		
		
		public Builder title(String title){
			this.title = title;
			return this;
		}
		
		public Builder status(int status){
			this.status = status;
			return this;
		}
		
		public Builder detail(String detail){
			this.detail = detail;
			return this;
		}
		
		public Builder timestamp(long timestamp){
			this.timestamp = timestamp;
			return this;
		}
		
		public Builder developerMessager(String message){
			this.developerMessage = message;
			return this;
		}
		
		public Builder field(String field){
			this.field = field;
			return this;
		}
		public Builder fieldMessage(String fieldMessage){
			this.fieldMessage = fieldMessage;
			return this;
		}
		
		public ValidationErrorDetails build(){
			return new ValidationErrorDetails(title, status, detail, timestamp, developerMessage, field, fieldMessage);
		}
		
	}
}
