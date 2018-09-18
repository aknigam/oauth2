package com.sample.oauth.oauth;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableAuthorizationServer;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableResourceServer;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;


/**
 * Refer: https://dzone.com/articles/secure-spring-rest-with-spring-security-and-oauth2
 *
 * Important classes can be found in the following package
 * package org.springframework.security.oauth2.config.annotation.web.configuration;
 */

@EnableWebMvc
@EnableAuthorizationServer
@EnableResourceServer
@SpringBootApplication
public class OauthApplication {

	public static void main(String[] args) {
		SpringApplication.run(OauthApplication.class, args);
	}
}
