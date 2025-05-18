package com.gyp.salechannelservice.services;

import java.util.List;
import java.util.Optional;

import com.gyp.salechannelservice.dtos.ticketshop.TicketShopRequestDto;
import com.gyp.salechannelservice.dtos.ticketshop.TicketShopResponseDto;

public interface TicketShopService {
	TicketShopResponseDto createTicketShop(TicketShopRequestDto ticketShop, String channelId);

	TicketShopResponseDto updateTicketShop(String id, TicketShopRequestDto ticketShop);

	Optional<TicketShopResponseDto> getTicketShopById(String id);

	Optional<TicketShopResponseDto> getTicketShopBySubdomain(String subdomain);

	Optional<TicketShopResponseDto> getTicketShopByCustomDomain(String customDomain);

	List<TicketShopResponseDto> getAllTicketShops();

	List<TicketShopResponseDto> getTicketShopsByOrganizerId(String organizerId);

	boolean isSubdomainAvailable(String subdomain);

	boolean isCustomDomainAvailable(String customDomain);

	void applyTemplate(String shopId, String templateId);

	void deleteTicketShop(String id);
}
