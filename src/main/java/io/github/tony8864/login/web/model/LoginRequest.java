package io.github.tony8864.login.web.model;

public record LoginRequest(String idToken) {
    public LoginRequest {
        if (idToken == null || idToken.isBlank()) {
            throw new IllegalArgumentException("ID token cannot be null");
        }
    }
}
