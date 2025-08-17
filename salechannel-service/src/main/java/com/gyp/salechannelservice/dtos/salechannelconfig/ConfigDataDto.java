package com.gyp.salechannelservice.dtos.salechannelconfig;

import java.util.List;

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
public class ConfigDataDto {
	private String theme;
	private String primaryColor;
	private String secondaryColor;
	private String fontFamily;

	private String siteTitle;
	private String siteLogoUrl;
	private String faviconUrl;

	private String contactEmail;
	private String contactPhone;
	private List<String> socialLinks;

	private String heroTitle;
	private String heroSubtitle;
	private String heroImageUrl;

	// SEO
	private String metaTitle;
	private String metaDescription;
	private List<String> metaKeywords;

	private boolean enableSearchEvents;
	private boolean enableLogin;
	private boolean enableMultiLanguage;
	private List<String> supportedLanguages;

	// Footer
	private String footerText;
	private List<String> footerLinks;
}
