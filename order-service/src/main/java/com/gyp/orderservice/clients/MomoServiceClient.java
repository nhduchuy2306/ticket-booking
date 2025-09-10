package com.gyp.orderservice.clients;

import java.util.Map;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@FeignClient(name = "momo-service", url = "${momo.endpoint}")
public interface MomoServiceClient {
	@PostMapping(value = "", consumes = MediaType.APPLICATION_JSON_VALUE)
	Object processPayment(@RequestBody Map<String, String> paymentRequest);
}
