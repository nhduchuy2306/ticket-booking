package com.gyp.salechannelservice.dtos.salechannelconfig;

import java.util.List;

import com.gyp.common.enums.salechannel.SaleChannelType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ApiPartnerSaleChannelConfigDto extends BaseSaleChannelConfigDto {
	private SaleChannelType type = SaleChannelType.API_PARTNER;
	private String apiKey;
	private String apiSecret;
	private String webhookUrl;

	private int rateLimit;                // request per second
	private boolean enableSandboxMode;
	private List<String> allowedIpRanges; // IP whitelist
}
