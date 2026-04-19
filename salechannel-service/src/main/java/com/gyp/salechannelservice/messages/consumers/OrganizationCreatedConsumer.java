package com.gyp.salechannelservice.messages.consumers;

import com.gyp.common.converters.Serialization;
import com.gyp.common.enums.salechannel.SaleChannelStatus;
import com.gyp.common.enums.salechannel.SaleChannelType;
import com.gyp.common.kafkatopics.TopicConstants;
import com.gyp.common.models.OrganizationCreatedEventModel;
import com.gyp.salechannelservice.dtos.salechannelconfig.TicketShopSaleChannelConfigDto;
import com.gyp.salechannelservice.entities.SaleChannelEntity;
import com.gyp.salechannelservice.repositories.SaleChannelRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Slf4j
@Component
@RequiredArgsConstructor
public class OrganizationCreatedConsumer {
	private final SaleChannelRepository saleChannelRepository;

	@KafkaListener(topics = TopicConstants.ORGANIZATION_CREATED_EVENT)
	public void createDefaultSaleChannel(String message) {
		try {
			OrganizationCreatedEventModel eventModel = Serialization.deserializeFromString(message,
					OrganizationCreatedEventModel.class);
			if(eventModel == null || StringUtils.isBlank(eventModel.getOrganizationId())) {
				return;
			}
			if(saleChannelRepository.findFirstByTypeAndOrganizationId(SaleChannelType.TICKET_SHOP,
					eventModel.getOrganizationId()).isPresent()) {
				return;
			}
			TicketShopSaleChannelConfigDto config = new TicketShopSaleChannelConfigDto();
			config.setType(SaleChannelType.TICKET_SHOP);
			config.setDisplayName(eventModel.getOrgName());
			config.setShowPoweredByGYP(Boolean.TRUE);
			SaleChannelEntity entity = new SaleChannelEntity();
			entity.setOrganizationId(eventModel.getOrganizationId());
			entity.setOrganizationSlug(eventModel.getOrgSlug());
			entity.setName(StringUtils.defaultIfBlank(eventModel.getOrgName(), "Ticket Shop"));
			entity.setType(SaleChannelType.TICKET_SHOP);
			entity.setStatus(SaleChannelStatus.ACTIVE);
			entity.setStartSaleAt(LocalDateTime.now());
			entity.setEndSaleAt(LocalDateTime.now().plusYears(1));
			entity.setSaleChannelConfig(Serialization.serializeToString(config));
			saleChannelRepository.save(entity);
			log.info("Created default ticket shop sale channel for org {}", eventModel.getOrganizationId());
		} catch(Exception e) {
			log.error("Failed to create default sale channel: {}", e.getMessage());
			throw new RuntimeException(e);
		}
	}
}

