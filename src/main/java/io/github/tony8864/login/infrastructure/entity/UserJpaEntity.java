package io.github.tony8864.login.infrastructure.entity;

import io.github.tony8864.login.domain.AuthProvider;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Id;
import lombok.Getter;

import java.time.Instant;
import java.util.UUID;

@Entity
@Getter
public class UserJpaEntity {

    protected UserJpaEntity() {}

    public UserJpaEntity(
            UUID id,
            AuthProvider authProvider,
            String providerUserId,
            Instant createdAt
    ) {
        this.id = id;
        this.authProvider = authProvider;
        this.providerUserId = providerUserId;
        this.createdAt = createdAt;
    }

    @Id
    private UUID id;

    @Enumerated(EnumType.STRING)
    private AuthProvider authProvider;
    private String providerUserId;
    private Instant createdAt;
}
