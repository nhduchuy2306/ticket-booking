package com.gyp.salechannelservice.services.impl;

import java.util.List;
import java.util.Optional;

import com.gyp.salechannelservice.dtos.channelpricing.ChannelPricingRequestDto;
import com.gyp.salechannelservice.dtos.channelpricing.ChannelPricingResponseDto;
import com.gyp.salechannelservice.repositories.SaleChannelRepository;
import com.gyp.salechannelservice.services.ChannelPricingService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ChannelPricingServiceImpl implements ChannelPricingService {
	private final SaleChannelRepository saleChannelRepository;

	@Override
	public ChannelPricingResponseDto createPricing(ChannelPricingRequestDto pricing) {
		return null;
	}

	@Override
	public ChannelPricingResponseDto updatePricing(String id, ChannelPricingRequestDto pricing) {
		return null;
	}

	@Override
	public Optional<ChannelPricingResponseDto> getPricingById(String id) {
		return Optional.empty();
	}

	@Override
	public List<ChannelPricingResponseDto> getPricingByChannelId(String channelId) {
		return null;
	}

	@Override
	public List<ChannelPricingResponseDto> getPricingByTicketTypeId(String ticketTypeId) {
		return null;
	}

	@Override
	public Optional<ChannelPricingResponseDto> getActivePricingForTicket(String channelId, String ticketTypeId) {
		return Optional.empty();
	}

	@Override
	public Double calculatePriceForTicket(String channelId, String ticketTypeId, Double basePrice) {
		return null;
	}

	@Override
	public void activatePricing(String id) {

	}

	@Override
	public void deactivatePricing(String id) {

	}

	@Override
	public void deletePricing(String id) {

	}
}
