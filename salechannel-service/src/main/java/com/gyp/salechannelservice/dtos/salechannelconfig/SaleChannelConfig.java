package com.gyp.salechannelservice.dtos.salechannelconfig;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.gyp.common.enums.salechannel.SaleChannelType;

@JsonTypeInfo(
		use = JsonTypeInfo.Id.NAME,
		include = JsonTypeInfo.As.PROPERTY,
		property = "type",
		visible = true
)
@JsonSubTypes({
		@JsonSubTypes.Type(value = TicketShopSaleChannelConfigDto.class, name = "TICKET_SHOP"),
		@JsonSubTypes.Type(value = BoxOfficeSaleChannelConfigDto.class, name = "BOX_OFFICE"),
		@JsonSubTypes.Type(value = ApiPartnerSaleChannelConfigDto.class, name = "API_PARTNER"),
		@JsonSubTypes.Type(value = MobileAppSaleChannelConfigDto.class, name = "MOBILE_APP")
})
public interface SaleChannelConfig {
	SaleChannelType getType();

	void setType(SaleChannelType type);
}
