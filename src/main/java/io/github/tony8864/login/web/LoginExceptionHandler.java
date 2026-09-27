package io.github.tony8864.login.web;

import io.github.tony8864.login.application.exception.IdentityVerificationException;
import io.github.tony8864.login.application.exception.IdentityVerificationUnavailableException;
import io.github.tony8864.login.application.exception.InvalidIdentityTokenException;
import io.github.tony8864.login.application.exception.TokenIssuingException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class LoginExceptionHandler {

    private static final Logger log = LoggerFactory.getLogger(LoginExceptionHandler.class);

    @ExceptionHandler(InvalidIdentityTokenException.class)
    public ProblemDetail handleInvalidIdentityToken() {
        ProblemDetail problemDetail = ProblemDetail.forStatusAndDetail(
                HttpStatus.UNAUTHORIZED,
                "The supplied identity token is invalid."
        );

        problemDetail.setTitle("Authentication failed");
        problemDetail.setProperty("code", "INVALID_IDENTITY_TOKEN");

        return problemDetail;
    }

    @ExceptionHandler(IdentityVerificationUnavailableException.class)
    public ProblemDetail handleIdentityVerificationUnavailable() {
        ProblemDetail problemDetail = ProblemDetail.forStatusAndDetail(
                HttpStatus.SERVICE_UNAVAILABLE,
                "Identity verification is temporarily unavailable."
        );

        problemDetail.setTitle("Identity verification unavailable");
        problemDetail.setProperty("code", "IDENTITY_VERIFICATION_UNAVAILABLE");

        return problemDetail;
    }

    @ExceptionHandler(IdentityVerificationException.class)
    public ProblemDetail handleIdentityVerificationException() {
        ProblemDetail problemDetail = ProblemDetail.forStatusAndDetail(
                HttpStatus.INTERNAL_SERVER_ERROR,
                "The identity token could not be verified due to an interal error."
        );

        problemDetail.setTitle("Identity verification failed");
        problemDetail.setProperty("code", "IDENTITY_VERIFICATION_FAILED");

        return problemDetail;
    }

    @ExceptionHandler(TokenIssuingException.class)
    public ProblemDetail handleTokenIssuing() {
        ProblemDetail problemDetail = ProblemDetail.forStatusAndDetail(
                HttpStatus.INTERNAL_SERVER_ERROR,
                "The login request could not be completed due to an internal error."
        );

        problemDetail.setTitle("Login failed");
        problemDetail.setProperty("code", "LOGIN_PROCESSING_FAILED");

        return problemDetail;
    }
}
