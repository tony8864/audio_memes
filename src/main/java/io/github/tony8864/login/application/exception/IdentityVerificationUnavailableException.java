package io.github.tony8864.login.application.exception;

public class IdentityVerificationUnavailableException extends IdentityVerificationException {
    public IdentityVerificationUnavailableException(String message) {
        super(message);
    }

    public IdentityVerificationUnavailableException(String message, Throwable cause) {
        super(message, cause);
    }
}
