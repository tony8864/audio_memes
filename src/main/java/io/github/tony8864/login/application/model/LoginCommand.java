package io.github.tony8864.login.application.model;

public record LoginCommand(String idToken) {
    public LoginCommand {
        if (idToken == null || idToken.isBlank()) {
            throw new IllegalArgumentException("Id token cannot be null");
        }
    }
}
