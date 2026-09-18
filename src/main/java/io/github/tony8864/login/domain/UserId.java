package io.github.tony8864.login.domain;

import java.util.UUID;

public record UserId(
        UUID value
) {
    public UserId {
        if (value == null) {
            throw new IllegalArgumentException("User ID cannot be null");
        }
    }

    public static UserId newId() {
        return new UserId(UUID.randomUUID());
    }
}
