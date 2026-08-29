package com.gyp.common.mappers;

import java.time.LocalDateTime;

import com.gyp.common.dtos.AbstractDto;
import com.gyp.common.entities.AbstractEntity;
import org.apache.commons.lang3.ObjectUtils;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

public interface AbstractMapper {
	default void mapAbstractFields(AbstractEntity source, AbstractDto target) {
		if(source == null || target == null) {
			return;
		}

		target.setCreateUser(source.getCreateUser());
		target.setChangeUser(source.getChangeUser());
		target.setCreateTimestamp(source.getCreateTimestamp());
		target.setChangeTimestamp(source.getChangeTimestamp());
	}

	default void mapAbstractFieldsToEntity(AbstractEntity target) {
		if(target == null) {
			return;
		}

		target.setChangeUser(getCurrentUserSafely());
		target.setChangeTimestamp(LocalDateTime.now());

		if(ObjectUtils.isEmpty(target.getCreateTimestamp())) {
			target.setCreateTimestamp(LocalDateTime.now());
		}

		if(ObjectUtils.isEmpty(target.getCreateUser())) {
			target.setCreateUser(getCurrentUserSafely());
		}
	}

	default String getCurrentUserSafely() {
		try {
			Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
			if(authentication != null && !"anonymousUser".equals(authentication.getPrincipal().toString())) {
				return authentication.getName();
			}
		} catch(Exception ignored) {

		}

		return "system";
	}
}
