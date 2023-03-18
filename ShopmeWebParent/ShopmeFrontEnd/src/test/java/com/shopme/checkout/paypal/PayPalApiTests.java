package com.shopme.checkout.paypal;

import java.util.Arrays;

import org.junit.jupiter.api.Test;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

public class PayPalApiTests {
	private static final String BASE_URL = "https://api.sandbox.paypal.com";
	private static final String GET_ORDER_API = "/v2/checkout/orders/";
	private static final String CLIENT_ID = "AWZVikIGh6rNyczW1U7zCcXW3PhE8qofcGRVvzgr67ZJkorJHpj26YODBEs1uCMeXYBmE965oK7d_oqZ";
	private static final String CLIENT_SECRET = "EAAQOSai-WuMlxZJuN59UF_ObTao7mwG--SH8l8S6iDjVan1Gc5FVgwiyeLdCU_yoBqOajXlB_ACfqm6";
	
	@Test
	public void testGetOrderDetails() {
		String orderId = "59E89616R8145140F";
		String requestURL = BASE_URL + GET_ORDER_API + orderId;
		
		HttpHeaders headers = new HttpHeaders();
		headers.setAccept(Arrays.asList(MediaType.APPLICATION_JSON));
		headers.add("Accept-Language", "en_US");
		headers.setBasicAuth(CLIENT_ID, CLIENT_SECRET);
		
		HttpEntity<MultiValueMap<String, String>> request = new HttpEntity<>(headers);
		RestTemplate restTemplate = new RestTemplate();
		
//		ResponseEntity<String> response = restTemplate.exchange(
//				requestURL, HttpMethod.GET, request, String.class);
//		
//		System.out.println(response);
		
		ResponseEntity<PayPalOrderResponse> response = restTemplate.exchange(
				requestURL, HttpMethod.GET, request, PayPalOrderResponse.class);
		PayPalOrderResponse orderResponse = response.getBody();

		System.out.println("Order ID: " + orderResponse.getId());
		System.out.println("Validated: " + orderResponse.validate(orderId));
		
	}
}