package io.github.tony8864.login.infrastructure.persistence;

import io.github.tony8864.login.domain.AuthProvider;
import io.github.tony8864.login.infrastructure.persistence.entity.UserJpaEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface SpringDataUserRepository extends JpaRepository<UserJpaEntity, UUID> {
    Optional<UserJpaEntity> findByAuthProviderAndProviderUserId(
            AuthProvider provider,
            String providerUserId
    );
}
