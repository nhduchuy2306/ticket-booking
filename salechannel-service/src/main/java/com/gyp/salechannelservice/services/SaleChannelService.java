package com.gyp.salechannelservice.services;

import java.util.List;
import java.util.Optional;

import com.gyp.common.enums.salechannel.SaleChannelType;
import com.gyp.salechannelservice.dtos.salechannel.SaleChannelRequestDto;
import com.gyp.salechannelservice.dtos.salechannel.SaleChannelResponseDto;
import com.gyp.salechannelservice.dtos.salechannelconfig.SaleChannelConfig;

public interface SaleChannelService {
	SaleChannelResponseDto getSaleChannelBySlug(String orgSlug);

	SaleChannelResponseDto updateCurrentOrganizationConfig(SaleChannelConfig saleChannelConfig, String orgSlug);

	SaleChannelResponseDto createSaleChannel(SaleChannelRequestDto saleChannel);

	SaleChannelResponseDto updateSaleChannel(String id, SaleChannelRequestDto saleChannel);

	Optional<SaleChannelResponseDto> getSaleChannelById(String id);

	List<SaleChannelResponseDto> getAllSaleChannels();

	List<SaleChannelResponseDto> getAllSaleChannelsByEventId(String eventId);

	List<SaleChannelResponseDto> getActiveSaleChannels();

	List<SaleChannelResponseDto> getSaleChannelsByType(SaleChannelType type);

	void activateChannel(String id);

	void deactivateChannel(String id);

	void deleteSaleChannel(String id);
}
