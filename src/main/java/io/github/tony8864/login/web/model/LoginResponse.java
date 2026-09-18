package io.github.tony8864.login.web.model;

public record LoginResponse(String accessToken) {
    public LoginResponse {
        if (accessToken == null || accessToken.isBlank()) {
            throw new IllegalArgumentException("ID token cannot be null");
        }
    }
}
