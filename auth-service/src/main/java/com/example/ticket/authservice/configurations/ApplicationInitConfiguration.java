package com.example.ticket.authservice.configurations;

import java.util.List;

import com.example.ticket.authservice.entities.UserAccountEntity;
import com.example.ticket.authservice.entities.UserGroupEntity;
import com.example.ticket.authservice.repositories.UserAccountRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.ApplicationRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
@EnableAspectJAutoProxy
@RequiredArgsConstructor
public class ApplicationInitConfiguration {
	private final PasswordEncoder passwordEncoder;

	@Bean
	public ApplicationRunner applicationRunner(UserAccountRepository userAccountRepository) {
		return args -> {
			UserAccountEntity admin = userAccountRepository.findByName("Admin").orElse(null);
			if(admin == null) {
				UserGroupEntity userGroupEntity = UserGroupEntity.builder()
						.name("ROOT")
						.description("This is Admin role")
						.administrator(Boolean.TRUE)
						.userGroupPermissionsRaw("{\"usergrouppermissions\":{}}")
						.build();

				UserAccountEntity userAccount = UserAccountEntity.builder()
						.name("Admin")
						.username("Admin")
						.password(passwordEncoder.encode("12345"))
						.userGroupEntityList(List.of(userGroupEntity))
						.build();

				userAccountRepository.save(userAccount);
			}

			UserAccountEntity normal = userAccountRepository.findByName("Normal").orElse(null);
			if(normal == null) {
				UserGroupEntity userGroupEntity = UserGroupEntity.builder()
						.name("normal")
						.description("This is Normal role")
						.administrator(Boolean.FALSE)
						.userGroupPermissionsRaw(
								"{\"usergrouppermissions\":{\"permissionItems\":[{\"permissionitem\":{\"applicationId\":\"app.user\",\"actions\":[\"READ\"],\"uuid\":\"70ac16be-aeae-4ded-b7db-79d20e9ffe24\"}}]}}")
						.build();

				UserAccountEntity userAccount = UserAccountEntity.builder()
						.name("Normal")
						.username("Normal")
						.password(passwordEncoder.encode("12345"))
						.userGroupEntityList(List.of(userGroupEntity))
						.build();

				userAccountRepository.save(userAccount);
			}
		};
	}
}
