package com.gyp.salechannelservice.services;

import java.time.LocalDateTime;
import java.util.List;

import com.gyp.salechannelservice.dtos.channelsales.ChannelSalesRequestDto;
import com.gyp.salechannelservice.dtos.channelsales.ChannelSalesResponseDto;

public interface ChannelSalesService {
	ChannelSalesResponseDto recordSale(ChannelSalesRequestDto sale);

	List<ChannelSalesResponseDto> getSalesByChannelId(String channelId);

	List<ChannelSalesResponseDto> getSalesByEventId(String eventId);

	List<ChannelSalesResponseDto> getSalesByChannelAndEvent(String channelId, String eventId);

	List<ChannelSalesResponseDto> getSalesByDateRange(LocalDateTime startDate, LocalDateTime endDate);

	Double getTotalRevenueByChannel(String channelId);

	Integer getTotalTicketsSoldByEvent(String eventId);

	List<ChannelSalesResponseDto> generateSalesReport(String channelId, String eventId,
			LocalDateTime startDate, LocalDateTime endDate);
}
