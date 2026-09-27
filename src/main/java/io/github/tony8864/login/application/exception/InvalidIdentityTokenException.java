package io.github.tony8864.login.application.exception;

public class InvalidIdentityTokenException extends IdentityVerificationException {
    public InvalidIdentityTokenException(String message) {
        super(message);
    }
}
