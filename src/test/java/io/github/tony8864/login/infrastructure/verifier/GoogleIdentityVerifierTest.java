package io.github.tony8864.login.infrastructure.verifier;

import com.google.api.client.googleapis.auth.oauth2.GoogleIdToken;
import com.google.api.client.googleapis.auth.oauth2.GoogleIdTokenVerifier;
import io.github.tony8864.login.application.exception.IdentityVerificationException;
import io.github.tony8864.login.application.exception.IdentityVerificationUnavailableException;
import io.github.tony8864.login.application.exception.InvalidIdentityTokenException;
import io.github.tony8864.login.domain.AuthProvider;
import io.github.tony8864.login.domain.ExternalIdentity;
import io.github.tony8864.login.infrastructure.verifier.google.GoogleIdentityVerifier;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.io.IOException;
import java.security.GeneralSecurityException;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class GoogleIdentityVerifierTest {

    @Mock
    private GoogleIdTokenVerifier googleIdTokenVerifier;

    @Mock
    private GoogleIdToken googleIdToken;

    private GoogleIdentityVerifier verifier;

    @BeforeEach
    void setup() {
        verifier = new GoogleIdentityVerifier(googleIdTokenVerifier);
    }

    @Test
    void verify_validToken_returnsExternalIdentity() throws GeneralSecurityException, IOException {
        // arrange
        GoogleIdToken.Payload payload = new GoogleIdToken.Payload();
        payload.setSubject("google-user-123");

        when(googleIdTokenVerifier.verify("idToken")).thenReturn(googleIdToken);
        when(googleIdToken.getPayload()).thenReturn(payload);

        // act
        ExternalIdentity externalIdentity = verifier.verify("idToken");

        // assert
        assertEquals(AuthProvider.GOOGLE, externalIdentity.provider());
        assertEquals("google-user-123", externalIdentity.providerUserId().value());
    }

    @Test
    void verify_invalidToken_throwsInvalidIdentityTokenException() throws GeneralSecurityException, IOException {
        // arrange
        when(googleIdTokenVerifier.verify("idToken")).thenReturn(null);

        // act
        InvalidIdentityTokenException ex = assertThrows(
                InvalidIdentityTokenException.class,
                () -> verifier.verify("idToken")
        );

        // assert
        assertEquals("Invalid Google Id token", ex.getMessage());
    }

    @Test
    void verify_ioException_throwsIdentityVerificationUnavailableException() throws GeneralSecurityException, IOException {
        // arrange
        IOException ioException = new IOException();
        when(googleIdTokenVerifier.verify("idToken")).thenThrow(ioException);

        // act
        IdentityVerificationUnavailableException ex = assertThrows(
                IdentityVerificationUnavailableException.class,
                () -> verifier.verify("idToken")
        );

        // assert
        assertEquals("Google identity verification is temporarily unavailable", ex.getMessage());
        assertEquals(ioException, ex.getCause());
    }

    @Test
    void verify_generalSecurityException_throwsIdentityVerificationException() throws GeneralSecurityException, IOException {
        // arrange
        GeneralSecurityException generalSecurityException = new GeneralSecurityException();
        when(googleIdTokenVerifier.verify("idToken")).thenThrow(generalSecurityException);

        // act
        IdentityVerificationException ex = assertThrows(
                IdentityVerificationException.class,
                () -> verifier.verify("idToken")
        );

        // assert
        assertEquals("Failed to securely verify Google ID token", ex.getMessage());
        assertEquals(generalSecurityException, ex.getCause());
    }
}