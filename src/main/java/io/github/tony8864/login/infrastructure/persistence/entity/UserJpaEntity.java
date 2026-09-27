package io.github.tony8864.login.infrastructure.persistence.entity;

import io.github.tony8864.login.domain.AuthProvider;
import jakarta.persistence.*;
import lombok.Getter;

import java.time.Instant;
import java.util.UUID;

@Entity
@Getter
@Table(name = "users")
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
    @Column(name = "id", nullable = false, updatable = false)
    private UUID id;

    @Enumerated(EnumType.STRING)
    @Column(name = "auth_provider", nullable = false, updatable = false)
    private AuthProvider authProvider;

    @Column(name = "provider_user_id", nullable = false, updatable = false)
    private String providerUserId;

    @Column(name = "created_at", nullable = false, updatable = false)
    private Instant createdAt;
}
