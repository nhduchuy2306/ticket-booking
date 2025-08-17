package com.gyp.salechannelservice.services;

public interface SaleChannelEventService {
	void assignEventToChannel(String channelId, String eventId);

	void removeEventFromChannel(String channelId, String eventId);
}
