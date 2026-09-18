package io.github.tony8864.login.application.model;

public record LoginResult(AccessToken accessToken) {
    public LoginResult {
        if (accessToken == null) {
            throw new IllegalArgumentException("Access token cannot be null");
        }
    }
}
