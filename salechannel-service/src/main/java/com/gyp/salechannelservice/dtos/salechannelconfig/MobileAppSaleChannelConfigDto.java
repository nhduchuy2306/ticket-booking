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
public class MobileAppSaleChannelConfigDto extends BaseSaleChannelConfigDto {
	private SaleChannelType type = SaleChannelType.MOBILE_APP;
	private String appName;
	private String appLogoUrl;

	private String appStoreUrl;
	private String playStoreUrl;

	private boolean enableLogin;
	private boolean enableSocialLogin;
	private List<String> supportedSocialProviders;

	private boolean enablePushNotifications;
	private String pushProvider;// FCM, APNs
	private String pushApiKey;// key for provider

	private List<String> supportedLanguages;
}
