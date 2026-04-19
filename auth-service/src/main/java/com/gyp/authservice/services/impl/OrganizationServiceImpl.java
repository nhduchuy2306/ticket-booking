package com.gyp.authservice.services.impl;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.List;

import com.fasterxml.jackson.core.type.TypeReference;
import com.gyp.authservice.dtos.organization.OrganizationRequestDto;
import com.gyp.authservice.dtos.organization.OrganizationResponseDto;
import com.gyp.authservice.entities.OrganizationEntity;
import com.gyp.authservice.entities.UserAccountEntity;
import com.gyp.authservice.entities.UserGroupEntity;
import com.gyp.authservice.mappers.OrganizationMapper;
import com.gyp.authservice.repositories.OrganizationRepository;
import com.gyp.authservice.repositories.UserAccountRepository;
import com.gyp.authservice.repositories.UserGroupRepository;
import com.gyp.authservice.services.AuthRedisCacheService;
import com.gyp.authservice.services.OrganizationService;
import com.gyp.common.converters.Serialization;
import com.gyp.common.enums.auth.RealmTypeEnum;
import com.gyp.common.exceptions.ResourceDuplicateException;
import com.gyp.common.exceptions.ResourceNotFoundException;
import com.gyp.common.kafkatopics.TopicConstants;
import com.gyp.common.models.OrganizationCreatedEventModel;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@RequiredArgsConstructor
public class OrganizationServiceImpl implements OrganizationService {
	private static final Duration ORGANIZATION_TTL = Duration.ofMinutes(10);
	private static final String ORGANIZATION_LIST_KEY = "auth:organization:list";
	private static final String ORGANIZATION_KEY_PREFIX = "auth:organization:";

	private final OrganizationRepository organizationRepository;
	private final UserAccountRepository userAccountRepository;
	private final UserGroupRepository userGroupRepository;
	private final OrganizationMapper organizationMapper;
	private final AuthRedisCacheService authRedisCacheService;
	private final PasswordEncoder passwordEncoder;
	private final KafkaTemplate<String, String> kafkaTemplate;
	private final JavaMailSender mailSender;

	@Override
	public List<OrganizationResponseDto> getAllOrganizations() {
		List<OrganizationResponseDto> cachedOrganizations = authRedisCacheService.get(ORGANIZATION_LIST_KEY,
				new TypeReference<>() {});
		if(cachedOrganizations != null) {
			return cachedOrganizations;
		}
		List<OrganizationEntity> organizationEntities = organizationRepository.findAll();
		List<OrganizationResponseDto> organizations = organizationEntities.stream()
				.map(organizationMapper::toResponseDto)
				.toList();
		authRedisCacheService.put(ORGANIZATION_LIST_KEY, organizations, ORGANIZATION_TTL);
		return organizations;
	}

	@Override
	@Transactional(rollbackFor = Exception.class)
	public OrganizationResponseDto registerOrganization(OrganizationRequestDto organizationRequestDto) {
		validateRegisterRequest(organizationRequestDto);
		OrganizationEntity organizationEntity = organizationMapper.toEntity(organizationRequestDto);
		organizationEntity.setStatus("ACTIVE");
		organizationEntity = organizationRepository.save(organizationEntity);

		UserAccountEntity owner = createOwnerAccount(organizationRequestDto, organizationEntity.getId());
		sendWelcomeMail(organizationEntity, owner.getEmail(), organizationRequestDto.getOwnerPassword());
		publishOrganizationCreatedEvent(organizationEntity, owner.getEmail());

		OrganizationResponseDto responseDto = organizationMapper.toResponseDto(organizationEntity);
		responseDto.setMessage("Organization registered successfully");
		authRedisCacheService.evict(ORGANIZATION_LIST_KEY);
		authRedisCacheService.put(organizationKey(responseDto.getId()), responseDto, ORGANIZATION_TTL);
		return responseDto;
	}

	@Override
	public OrganizationResponseDto getOrganizationById(String id) {
		OrganizationResponseDto cachedOrganization = authRedisCacheService.get(organizationKey(id),
				OrganizationResponseDto.class);
		if(cachedOrganization != null) {
			return cachedOrganization;
		}
		OrganizationEntity organizationEntity = organizationRepository.findById(id).orElse(null);
		if(organizationEntity != null) {
			OrganizationResponseDto responseDto = organizationMapper.toResponseDto(organizationEntity);
			authRedisCacheService.put(organizationKey(id), responseDto, ORGANIZATION_TTL);
			return responseDto;
		}
		return null;
	}

	@Override
	public OrganizationResponseDto createOrganization(OrganizationRequestDto organizationRequestDto) {
		OrganizationEntity organizationEntity = organizationRepository.save(organizationMapper.toEntity(organizationRequestDto));
		OrganizationResponseDto responseDto = organizationMapper.toResponseDto(organizationEntity);
		authRedisCacheService.evict(ORGANIZATION_LIST_KEY);
		authRedisCacheService.put(organizationKey(responseDto.getId()), responseDto, ORGANIZATION_TTL);
		return responseDto;
	}

	@Override
	public OrganizationResponseDto updateOrganization(String id, OrganizationRequestDto organizationRequestDto) {
		OrganizationEntity organizationEntity = organizationRepository.findById(id)
				.orElseThrow(() -> new ResourceNotFoundException("Organization not found"));
		organizationMapper.updateEntityFromDto(organizationRequestDto, organizationEntity);
		organizationEntity = organizationRepository.save(organizationEntity);
		OrganizationResponseDto responseDto = organizationMapper.toResponseDto(organizationEntity);
		authRedisCacheService.evict(ORGANIZATION_LIST_KEY);
		authRedisCacheService.put(organizationKey(id), responseDto, ORGANIZATION_TTL);
		return responseDto;
	}

	@Override
	public void deleteOrganization(String id) {
		if(!organizationRepository.existsById(id)) {
			throw new ResourceNotFoundException("Organization not found");
		}
		organizationRepository.deleteById(id);
		authRedisCacheService.evict(ORGANIZATION_LIST_KEY);
		authRedisCacheService.evict(organizationKey(id));
	}

	private void validateRegisterRequest(OrganizationRequestDto request) {
		if(request == null) {
			throw new IllegalArgumentException("Organization registration payload is required");
		}
		if(request.getOwnerPassword() == null || !request.getOwnerPassword().equals(request.getConfirmPassword())) {
			throw new IllegalArgumentException("Owner password and confirm password do not match");
		}
		if(organizationRepository.existsByOrgSlug(request.getOrgSlug())) {
			throw new ResourceDuplicateException("Organization slug already exists: " + request.getOrgSlug());
		}
		if(userAccountRepository.existsByEmail(request.getOwnerEmail())) {
			throw new ResourceDuplicateException("Owner email already exists: " + request.getOwnerEmail());
		}
	}

	private UserAccountEntity createOwnerAccount(OrganizationRequestDto request, String organizationId) {
		UserGroupEntity ownerGroup = new UserGroupEntity();
		ownerGroup.setName("ORGANIZATION_OWNER");
		ownerGroup.setDescription("Organization owner role");
		ownerGroup.setAdministrator(Boolean.TRUE);
		ownerGroup.setOrganizationId(organizationId);
		ownerGroup.setUserGroupPermissionsRaw("{}");
		ownerGroup = userGroupRepository.save(ownerGroup);

		UserAccountEntity owner = new UserAccountEntity();
		owner.setName(request.getOwnerFullName());
		owner.setUsername(request.getOwnerEmail());
		owner.setEmail(request.getOwnerEmail());
		owner.setPassword(passwordEncoder.encode(request.getOwnerPassword()));
		owner.setPhoneNumber(request.getRepresentativePhone());
		owner.setDob(LocalDateTime.now());
		owner.setRealmType(RealmTypeEnum.GYP_DEFAULT_REALM);
		owner.setOrganizationId(organizationId);
		owner.setUserGroupEntityList(List.of(ownerGroup));
		return userAccountRepository.save(owner);
	}

	private void publishOrganizationCreatedEvent(OrganizationEntity organizationEntity, String ownerEmail) {
		OrganizationCreatedEventModel eventModel = OrganizationCreatedEventModel.builder()
				.organizationId(organizationEntity.getId())
				.orgSlug(organizationEntity.getOrgSlug())
				.orgName(organizationEntity.getName())
				.ownerEmail(ownerEmail)
				.build();
		kafkaTemplate.send(TopicConstants.ORGANIZATION_CREATED_EVENT, Serialization.serializeToString(eventModel));
	}

	private void sendWelcomeMail(OrganizationEntity organizationEntity, String ownerEmail, String temporaryPassword) {
		try {
			SimpleMailMessage message = new SimpleMailMessage();
			message.setFrom("no-reply@gyp.local");
			message.setTo(ownerEmail);
			message.setSubject("Welcome to GYP TicketBox");
			message.setText(String.format(
					"Welcome to GYP TicketBox!%n%nOrganization: %s%nLogin email: %s%nTemporary password: %s%nLogin: http://lvh.me:3000/login%n",
					organizationEntity.getName(), ownerEmail, temporaryPassword));
			mailSender.send(message);
		} catch(Exception e) {
			log.warn("Failed to send welcome mail for organization {}: {}", organizationEntity.getOrgSlug(), e.getMessage());
		}
	}

	private String organizationKey(String id) {
		return ORGANIZATION_KEY_PREFIX + id;
	}
}
