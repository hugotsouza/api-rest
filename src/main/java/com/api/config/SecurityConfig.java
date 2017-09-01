package com.api.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import com.api.service.CustomUserDetailService;

@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled=true)//enables secure spring methods and enables pre-authentication verification
public class SecurityConfig extends WebSecurityConfigurerAdapter{

	@Autowired
	private CustomUserDetailService custoUserDetailService;
	@Override
	protected void configure(HttpSecurity http) throws Exception {
		/***
		 * When you use defaults in naming URIs 
		 * setting permissions is simplified
		 */
		http.authorizeRequests()
			.antMatchers("/*/protected/**").hasRole("USER")
			.antMatchers("/*/admin/**").hasRole("ADMIN")
			.and()
			.httpBasic()//an authentication in the header is enough
			.and()
			.csrf()
			.disable();
	}
	/***
	 * Password encryption will be done using the BCryptPasswordEncoder class 
	 * because SHA1, SHA2, and MD5 are deprecated.
	 */
	@Override
	protected void configure(AuthenticationManagerBuilder auth) throws Exception {
		auth.userDetailsService(custoUserDetailService)
		.passwordEncoder(new BCryptPasswordEncoder());
	}
}