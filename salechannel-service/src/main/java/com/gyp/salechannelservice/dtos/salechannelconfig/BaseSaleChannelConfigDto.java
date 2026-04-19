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
	// Branding
	private String logoUrl;
	private String faviconUrl;
	private String primaryColor;
	private String secondaryColor;
	private String bannerUrl;

	// Identity
	private String displayName;
	private String tagline;
	private String supportEmail;

	// Social links
	private String facebookUrl;
	private String instagramUrl;
	private String websiteUrl;

	// Behavior
	private Boolean showPoweredByGYP;

	private String theme;
	private String fontFamily;

	private String siteLogoUrl;
	private String contactEmail;
	private String contactPhone;
}
