package io.github.tony8864.login.application.exception;

public class IdentityVerificationException extends RuntimeException {
    public IdentityVerificationException(String message) {
        super(message);
    }

    public IdentityVerificationException(String message, Throwable cause) {
        super(message, cause);
    }
}
