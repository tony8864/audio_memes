package io.github.tony8864.login.application.exception;

public class TokenIssuingException extends RuntimeException {
    public TokenIssuingException(String message, Throwable cause) {
        super(message, cause);
    }
}
