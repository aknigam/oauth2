package com.sample.oauth.oauth;

import org.apache.catalina.authenticator.BasicAuthenticator;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;

import org.springframework.web.client.RestTemplate;
import sun.misc.BASE64Encoder;

import java.util.Base64;
import java.util.Collections;
import java.util.List;
import java.util.Map;

public class OauthApplicationTests {


	private String clientId = "foo";
	private String clientSecret = "bar";

	private String user = "user";
	private String securityPassword = "af748ee4-0b68-41ac-b403-f899ce4fe2e3";

	@Test
	public void testResponseTypeTokenOauth(){

		RestTemplate restTemplate = new RestTemplate();
		HttpHeaders requestHeaders = new HttpHeaders();
		requestHeaders.setAccept(Collections.singletonList(new MediaType("application","json")));
		requestHeaders.set(HttpHeaders.AUTHORIZATION, "Basic "+getBase64EncodeAuth(user, securityPassword));
		HttpEntity<?> requestEntity = new HttpEntity<Object>( requestHeaders);


		String url = "http://localhost:8090/oauth/authorize?response_type=token&client_id=foo&redirect_uri=http://localhost:8080/hello&scope=scope&state=testing";
		ResponseEntity<String> r = restTemplate.postForEntity(url, requestEntity, String.class);

		System.out.println(r);

		String location = String.valueOf(r.getHeaders().get("Location"));

		// http://localhost:8080/hello#access_token=2a77524d-8b56-4c7f-851e-ecefc592af30&token_type=bearer&state=testing&expires_in=40787

		String accessToken = location.substring( location.indexOf("#"),
				location.indexOf("&"));
		accessToken = accessToken.split("=")[1];
		System.out.println(location);
		System.out.println(accessToken);

		restTemplate = new RestTemplate();
		requestHeaders = new HttpHeaders();
		requestHeaders.setAccept(Collections.singletonList(new MediaType("application","json")));
		requestHeaders.set(HttpHeaders.AUTHORIZATION, "bearer "+accessToken);
		requestEntity = new HttpEntity<String>( requestHeaders);

		ResponseEntity<String> response = restTemplate.exchange("http://localhost:8090/hello/anand", HttpMethod.GET,
				requestEntity, String.class);
		String helloResponse = response.getBody();
		System.out.println(helloResponse);



	}

	@Test
	public void testResponseTypeCodeOauth(){

		RestTemplate restTemplate = new RestTemplate();
		HttpHeaders requestHeaders = new HttpHeaders();
		requestHeaders.setAccept(Collections.singletonList(new MediaType("application","json")));


		requestHeaders.set(HttpHeaders.AUTHORIZATION, "Basic "+getBase64EncodeAuth(user, securityPassword));
		HttpEntity<?> requestEntity = new HttpEntity<Object>( requestHeaders);


		String url = "http://localhost:8090/oauth/authorize?response_type=code&client_id=foo&redirect_uri=http://localhost:8080/hello&scope=scope&state=testing";
		ResponseEntity<String> r = restTemplate.postForEntity(url, requestEntity, String.class);

		System.out.println(r);

		String location = String.valueOf(r.getHeaders().get("Location"));

		// http://localhost:8080/hello?code=8jdSy1&state=testing

		String tag = location.substring( location.indexOf("?"),
				location.indexOf("&"));
		tag = tag.split("=")[1];
		System.out.println(location);
		System.out.println(tag);


		// get the access token from the token endpoint

		String bearer = getAccessTokenFromTokenEndpoint(tag);


		restTemplate = new RestTemplate();
		requestHeaders = new HttpHeaders();
		requestHeaders.setAccept(Collections.singletonList(new MediaType("application","json")));
		requestHeaders.set(HttpHeaders.AUTHORIZATION, "bearer "+bearer);
		requestEntity = new HttpEntity<String>( requestHeaders);

		ResponseEntity<String> response = restTemplate.exchange("http://localhost:8090/hello/anand", HttpMethod.GET,
				requestEntity, String.class);
		String helloResponse = response.getBody();
		System.out.println(helloResponse);
	}

	private String getAccessTokenFromTokenEndpoint(String accessToken) {

		String url  = "http://localhost:8090/oauth/token?grant_type=authorization_code&code=%s&clientId=foo" +
				"&redirect_uri=http://localhost:8080/hello&scope=scope";
		url = String.format(url, accessToken);

		RestTemplate restTemplate = new RestTemplate();
		HttpHeaders requestHeaders = new HttpHeaders();
		requestHeaders.setAccept(Collections.singletonList(new MediaType("application","json")));

		String auth = getBase64EncodeAuth(clientId, clientSecret);
		requestHeaders.set(HttpHeaders.AUTHORIZATION, "Basic "+auth);
		HttpEntity<String> requestEntity = new HttpEntity<String>(requestHeaders);

		ResponseEntity<Map> response = restTemplate.exchange(url, HttpMethod.POST,
				requestEntity, Map.class);


		/**
		 * Sample response
		 *
		 {
		 "access_token": "0923efce-e21c-423f-89fb-36480711e0f4",
		 "token_type": "bearer",
		 "refresh_token": "7ba232dc-0428-42b5-8d71-f2ae97c23ac3",
		 "expires_in": 42225,
		 "scope": "scope"
		 }
		 */
		String bearer = (String) response.getBody().get("access_token");
		System.out.println("Bearer "+ bearer);
		return bearer;
	}

	private String getBase64EncodeAuth(String username, String password) {
		BASE64Encoder base64Encoder = new BASE64Encoder();
		String crd = username+":"+password;
		return base64Encoder.encode(crd.getBytes());
	}

}
