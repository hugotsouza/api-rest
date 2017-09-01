package com.api.handler;



import java.io.IOException;

import org.apache.commons.io.IOUtils;
import org.springframework.http.client.ClientHttpResponse;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.client.DefaultResponseErrorHandler;
/***
 * Intercepts the erros in response received for API.
 * 
 * @author Hugo Trindade
 *
 */
@ControllerAdvice
public class RestExceptionResponseHandler extends DefaultResponseErrorHandler{
	
	@Override
	public boolean hasError(ClientHttpResponse response) throws IOException {
		System.out.println("inside hasError");
		return super.hasError(response);
	}
	
	@Override
	public void handleError(ClientHttpResponse response) throws IOException {
		System.out.println("Doing something with statusCode: " + response.getStatusCode());
		System.out.println("Doing something with body: " + IOUtils.toString(response.getBody(), "UTF-8"));
		//super.handleError(response);
	}
}
