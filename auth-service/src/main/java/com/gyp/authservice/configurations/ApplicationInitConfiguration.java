package com.gyp.authservice.configurations;

import java.util.List;

import com.gyp.authservice.entities.UserAccountEntity;
import com.gyp.authservice.entities.UserGroupEntity;
import com.gyp.authservice.repositories.UserAccountRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.ApplicationRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
@RequiredArgsConstructor
public class ApplicationInitConfiguration {
	private static final String DEFAULT_SIMPLE_PASSWORD = "12345";
	private static final String ADMIN_NAME = "Admin";
	private static final String NORMAL_NAME = "Normal";
	private static final String ORGANIZER_NAME = "Organizer";

	private final PasswordEncoder passwordEncoder;

	@Bean
	public ApplicationRunner applicationRunner(UserAccountRepository userAccountRepository) {
		return args -> {
			createAdminUserIfNotExists(userAccountRepository);
			createNormalUserIfNotExists(userAccountRepository);
			createOrganizerIfNotExists(userAccountRepository);
		};
	}

	private void createAdminUserIfNotExists(UserAccountRepository repository) {
		if(repository.findByName(ADMIN_NAME).isEmpty()) {
			UserGroupEntity adminGroup = createUserGroup(
					"ROOT",
					"This is Admin role",
					true,
					"{}"
			);

			createAndSaveUser(repository, ADMIN_NAME, ADMIN_NAME, adminGroup);
		}
	}

	private void createNormalUserIfNotExists(UserAccountRepository repository) {
		if(repository.findByName(NORMAL_NAME).isEmpty()) {
			String normalPermissions = "{\"permissionItems\":[{\"applicationId\":\"app.user\",\"actions\":[\"READ\"],\"uuid\":\"70ac16be-aeae-4ded-b7db-79d20e9ffe24\"}]}";

			UserGroupEntity normalGroup = createUserGroup(
					"normal",
					"This is Normal role",
					false,
					normalPermissions
			);

			createAndSaveUser(repository, NORMAL_NAME, NORMAL_NAME, normalGroup);
		}
	}

	private void createOrganizerIfNotExists(UserAccountRepository repository) {
		if(repository.findByName(ORGANIZER_NAME).isEmpty()) {
			String organizerPermissions = "{\"permissionItems\":[{\"applicationId\":\"app.user\",\"actions\":[\"READ\"],\"uuid\":\"70ac16be-aeae-4ded-b7db-79d20e9ffe24\"},{\"applicationId\":\"app.event\",\"actions\":[\"CREATE\", \"READ\", \"UPDATE\", \"DELETE\"],\"uuid\":\"70ac16be-aeae-4ded-b7db-79d20e9ffe25\"}]}";

			UserGroupEntity organizerGroup = createUserGroup(
					"organizer",
					"This is Organizer role",
					false,
					organizerPermissions
			);

			createAndSaveUser(repository, ORGANIZER_NAME, ORGANIZER_NAME, organizerGroup);
		}
	}

	private UserGroupEntity createUserGroup(String name, String description,
			boolean isAdmin, String permissions) {
		return UserGroupEntity.builder()
				.name(name)
				.description(description)
				.administrator(isAdmin)
				.userGroupPermissionsRaw(permissions)
				.build();
	}

	private void createAndSaveUser(UserAccountRepository repository, String name,
			String username, UserGroupEntity userGroup) {
		UserAccountEntity user = UserAccountEntity.builder()
				.name(name)
				.username(username)
				.password(passwordEncoder.encode(DEFAULT_SIMPLE_PASSWORD))
				.userGroupEntityList(List.of(userGroup))
				.build();

		repository.save(user);
	}
}