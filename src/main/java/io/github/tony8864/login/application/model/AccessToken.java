package io.github.tony8864.login.application.model;

public record AccessToken(String value) {
    public AccessToken {
        if (value == null || value.isBlank()) {
            throw new IllegalArgumentException("Access token cannot be blank");
        }
    }
}
