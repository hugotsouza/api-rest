package com.api.error;

/***
 * Class with the default structure of an exception send for the client
 * @author Hugo
 *
 */
public class ErrorDetails {

	public ErrorDetails(String title, int status, String detail, long timestamp, String developerMessage) {
		this.title = title;
		this.status = status;
		this.detail = detail;
		this.timestamp = timestamp;
		this.developerMessage = developerMessage;
	}

	protected String title;
	protected int status;
	protected String detail;
	protected long timestamp;
	protected String developerMessage;

	public ErrorDetails() {
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
		
		public ErrorDetails build(){
			return new ErrorDetails(title, status, detail, timestamp, developerMessage);
		}
		
	}

	public String getTitle() {
		return title;
	}

	public int getStatus() {
		return status;
	}

	public String getDetail() {
		return detail;
	}

	public long getTimestamp() {
		return timestamp;
	}

	public String getDeveloperMessage() {
		return developerMessage;
	}

}