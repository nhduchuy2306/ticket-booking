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
public class BoxOfficeSaleChannelConfigDto extends BaseSaleChannelConfigDto {
	private SaleChannelType type = SaleChannelType.BOX_OFFICE;
	private boolean enableCashPayment;
	private boolean enableCardPayment;

	private String printerConfig;
	private String receiptFooterText;

	private List<String> staffAccounts;
	private boolean shiftReportEnabled;
	private int maxConcurrentTerminals;
}
