package com.gyp.orderservice.dtos.payment;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Getter
@Setter
@Component
@ConfigurationProperties(prefix = "momo")
public class MomoProperties {
	private String partnerCode;
	private String accessKey;
	private String secretKey;
	private String endpoint;
	private String returnUrl;
	private String notifyUrl;
	private String frontendSuccessUrl;
	private String frontendFailureUrl;
}
