package com.api.config;
/***
 * 
 * @author Hugo Trindade
 *
 */
public class SecurityConstants {
	
	static final String SECRET = "Kn322lkN4k322n";
	//because Authorizarion starts with 'Bearer '
	static final String TOKEN_PREFIX = "Bearer ";
	static final String HEADER_STRING = "Authorization";
	//the url will be public access
	static final String SIGN_UP_URL ="/users/sign-up";
	//one day in milliseconds
	static final long EXPIRATION_TIME = 86400000l;

	

}
