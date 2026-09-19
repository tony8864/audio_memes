package io.github.tony8864.login.domain;

import java.time.Instant;

public class User {
    private final UserId userId;
    private final Instant createdAt;
    private final ExternalIdentity externalIdentity;

    public User(
            UserId userId,
            ExternalIdentity externalIdentity,
            Instant createdAt
    ) {
        this.createdAt = createdAt;
        this.userId = userId;
        this.externalIdentity = externalIdentity;
    }

    public UserId userId() {
        return userId;
    }

    public ExternalIdentity externalIdentity() {
        return externalIdentity;
    }

    public Instant createdAt() {
        return createdAt;
    }
}
