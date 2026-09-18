package io.github.tony8864.login.domain;

public record ProviderUserId(String value) {
    public ProviderUserId {
        if (value == null || value.isBlank()) {
            throw new IllegalArgumentException("Provider user ID must not be blank");
        }
    }
}
