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
@NoArgsConstructor
@AllArgsConstructor
public class TicketShopSaleChannelConfigDto extends BaseSaleChannelConfigDto {
	private SaleChannelType type = SaleChannelType.TICKET_SHOP;
	private String siteUrl;
	private String siteTitle;
	private String faviconUrl;

	private String heroTitle;
	private String heroSubtitle;
	private String heroImageUrl;

	// SEO
	private String metaTitle;
	private String metaDescription;
	private List<String> metaKeywords;

	// Features
	private boolean enableSearchEvents;
	private boolean enableLogin;
	private boolean enableMultiLanguage;
	private List<String> supportedLanguages;

	// Payment
	private String paymentGateway;
	private List<String> supportedCurrencies;
	private boolean enableCoupons;
	private boolean enableAffiliateProgram;

	// Footer
	private String footerText;
	private List<String> footerLinks;
}
