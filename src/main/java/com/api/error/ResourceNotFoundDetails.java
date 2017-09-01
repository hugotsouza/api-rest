package com.api.error;
/***
 * Class created to be manipulated by class 
 * RestExceptionHandler with default structure for 
 * an exception of type ResourceNotFoundException 
 * sent to the client.
 * @author Hugo Trindade
 *
 */
public class ResourceNotFoundDetails extends ErrorDetails {

	public ResourceNotFoundDetails(String title, int status, String detail, long timestamp,
			String developerMessage) {
		this.title = title;
		this.status = status;
		this.detail = detail;
		this.timestamp = timestamp;
		this.developerMessage = developerMessage;
	}

	public static final class Builder{
		private String title;
		private int status;
		private String detail;
		private long timestamp;
		private String developerMessage;
		
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
		
		public ResourceNotFoundDetails build(){
			return new ResourceNotFoundDetails(title, status, detail, timestamp, developerMessage);
		}
		
	}
}
