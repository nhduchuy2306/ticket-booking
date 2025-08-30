package com.gyp.salechannelservice.dtos.salechannelconfig;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public abstract class BaseSaleChannelConfigDto implements SaleChannelConfig {
	private String theme;
	private String primaryColor;
	private String secondaryColor;
	private String fontFamily;

	private String siteLogoUrl;
	private String contactEmail;
	private String contactPhone;
}
