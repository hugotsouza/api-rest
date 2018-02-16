package com.api.config;

import static com.api.config.SecurityConstants.SIGN_UP_URL;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.cors.CorsConfiguration;

import com.api.service.CustomUserDetailService;

@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled=true)//enables secure spring methods and enables pre-authentication verification
public class SecurityConfig extends WebSecurityConfigurerAdapter{

	@Autowired
	private CustomUserDetailService customUserDetailService;
//	@Override
//	protected void configure(HttpSecurity http) throws Exception {
//		/***
//		 * When you use defaults in naming URIs 
//		 * setting permissions is simplified
//		 */
//		http.authorizeRequests()
//			.antMatchers("/*/protected/**").hasRole("USER")
//			.antMatchers("/*/admin/**").hasRole("ADMIN")
//			.and()
//			.httpBasic()//an authentication in the header is enough
//			.and()
//			.csrf()
//			.disable();
//	}
	
	@Override
	protected void configure(HttpSecurity http) throws Exception {
			
		http.cors()
		.configurationSource(request -> new CorsConfiguration().applyPermitDefaultValues())
		.and().csrf().disable().authorizeRequests()
		.antMatchers(HttpMethod.GET, SIGN_UP_URL).permitAll()
		.antMatchers("/*/protected/**").hasRole("USER")
		.antMatchers("/*/admin/**").hasRole("ADMIN")
		.and()
		.addFilter(new JWTAuthenticationFilter(authenticationManager()))
		.addFilter(new JWTAuthorizationFilter(authenticationManager(), customUserDetailService));
		
		
	}
	
	
	@Override
	protected void configure(AuthenticationManagerBuilder auth) throws Exception {
		auth.userDetailsService(customUserDetailService)
		.passwordEncoder(new BCryptPasswordEncoder());// Password encryption will be done using the BCryptPasswordEncoder class 
		 											  // because SHA1, SHA2, and MD5 are deprecated.
	}
}