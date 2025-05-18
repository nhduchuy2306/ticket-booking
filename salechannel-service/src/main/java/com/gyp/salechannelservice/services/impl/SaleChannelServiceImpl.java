package com.gyp.salechannelservice.services.impl;

import java.util.List;
import java.util.Optional;

import com.gyp.common.enums.salechannel.SaleChannelType;
import com.gyp.salechannelservice.dtos.salechannel.SaleChannelRequestDto;
import com.gyp.salechannelservice.dtos.salechannel.SaleChannelResponseDto;
import com.gyp.salechannelservice.services.SaleChannelService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class SaleChannelServiceImpl implements SaleChannelService {
	@Override
	public SaleChannelResponseDto createSaleChannel(SaleChannelRequestDto saleChannel) {
		return null;
	}

	@Override
	public SaleChannelResponseDto updateSaleChannel(String id, SaleChannelResponseDto saleChannel) {
		return null;
	}

	@Override
	public Optional<SaleChannelResponseDto> getSaleChannelById(String id) {
		return Optional.empty();
	}

	@Override
	public List<SaleChannelResponseDto> getAllSaleChannels() {
		return null;
	}

	@Override
	public List<SaleChannelResponseDto> getActiveSaleChannels() {
		return null;
	}

	@Override
	public List<SaleChannelResponseDto> getSaleChannelsByType(SaleChannelType type) {
		return null;
	}

	@Override
	public List<SaleChannelResponseDto> getSaleChannelsForEvent(String eventId) {
		return null;
	}

	@Override
	public void assignEventToChannel(String channelId, String eventId) {

	}

	@Override
	public void removeEventFromChannel(String channelId, String eventId) {

	}

	@Override
	public void activateChannel(String id) {

	}

	@Override
	public void deactivateChannel(String id) {

	}

	@Override
	public void deleteSaleChannel(String id) {

	}
}
