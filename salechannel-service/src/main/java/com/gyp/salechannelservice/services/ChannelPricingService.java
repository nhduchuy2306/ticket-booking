package com.gyp.salechannelservice.services;

import java.util.List;
import java.util.Optional;

import com.gyp.salechannelservice.dtos.channelpricing.ChannelPricingRequestDto;
import com.gyp.salechannelservice.dtos.channelpricing.ChannelPricingResponseDto;

public interface ChannelPricingService {
	ChannelPricingResponseDto createPricing(ChannelPricingRequestDto pricing);

	ChannelPricingResponseDto updatePricing(String id, ChannelPricingRequestDto pricing);

	Optional<ChannelPricingResponseDto> getPricingById(String id);

	List<ChannelPricingResponseDto> getPricingByChannelId(String channelId);

	List<ChannelPricingResponseDto> getPricingByTicketTypeId(String ticketTypeId);

	Optional<ChannelPricingResponseDto> getActivePricingForTicket(String channelId, String ticketTypeId);

	Double calculatePriceForTicket(String channelId, String ticketTypeId, Double basePrice);

	void activatePricing(String id);

	void deactivatePricing(String id);

	void deletePricing(String id);
}
