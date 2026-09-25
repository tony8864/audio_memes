package io.github.tony8864.login.infrastructure.persistence;

import io.github.tony8864.login.application.port.UserRepository;
import io.github.tony8864.login.domain.*;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.data.jpa.test.autoconfigure.DataJpaTest;
import org.springframework.boot.testcontainers.service.connection.ServiceConnection;
import org.springframework.context.annotation.Import;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import org.testcontainers.postgresql.PostgreSQLContainer;

import java.time.Instant;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest(properties = "spring.jpa.hibernate.ddl-auto=create-drop")
@Testcontainers
@Import(UserJpaRepositoryAdapter.class)
class UserJpaRepositoryAdapterTest {

    @Container
    @ServiceConnection
    static PostgreSQLContainer postgres = new PostgreSQLContainer("postgres:16");

    @Autowired
    UserRepository userRepository;

    @Test
    void save_thenFindByExternalIdentity_returnsUser() {
        ExternalIdentity externalIdentity = new ExternalIdentity(
                AuthProvider.GOOGLE,
                new ProviderUserId("google-user")
        );

        User user = new User(
                UserId.newId(),
                externalIdentity,
                Instant.now()
        );

        userRepository.save(user);

        Optional<User> result = userRepository.findByExternalIdentity(externalIdentity);

        assertTrue(result.isPresent());
        assertEquals(user.userId(), result.get().userId());
    }

    @Test
    void findByExternalIdentity_whenUserDoesNotExist_returnsEmpty() {
        ExternalIdentity externalIdentity = new ExternalIdentity(
                AuthProvider.GOOGLE,
                new ProviderUserId("google-user")
        );

        Optional<User> result = userRepository.findByExternalIdentity(externalIdentity);

        assertTrue(result.isEmpty());
    }


    @Test
    void findByExternalIdentity_distinguishesProviderAndProviderUserId() {
        ExternalIdentity googleIdentity =
                new ExternalIdentity(
                        AuthProvider.GOOGLE,
                        new ProviderUserId("123")
                );

        ExternalIdentity appleIdentity =
                new ExternalIdentity(
                        AuthProvider.APPLE,
                        new ProviderUserId("123")
                );

        User googleUser =
                new User(
                        new UserId(UUID.randomUUID()),
                        googleIdentity,
                        Instant.now()
                );

        User appleUser =
                new User(
                        new UserId(UUID.randomUUID()),
                        appleIdentity,
                        Instant.now()
                );

        userRepository.save(googleUser);
        userRepository.save(appleUser);

        Optional<User> googleResult =
                userRepository.findByExternalIdentity(googleIdentity);

        Optional<User> appleResult =
                userRepository.findByExternalIdentity(appleIdentity);

        assertTrue(googleResult.isPresent());
        assertTrue(appleResult.isPresent());

        assertEquals(
                googleUser.userId(),
                googleResult.get().userId()
        );

        assertEquals(
                appleUser.userId(),
                appleResult.get().userId()
        );
    }
}