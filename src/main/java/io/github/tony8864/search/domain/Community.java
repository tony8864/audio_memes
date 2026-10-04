package io.github.tony8864.search.domain;

import io.github.tony8864.login.domain.UserId;

public record Community(UserId userId) implements ClipCreator {
    public Community {
        if (userId == null) {
            throw new IllegalArgumentException("User ID cannot be null");
        }
    }
}
