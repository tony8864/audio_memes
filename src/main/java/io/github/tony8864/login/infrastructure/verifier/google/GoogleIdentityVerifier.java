package io.github.tony8864.login.infrastructure.verifier.google;

import com.google.api.client.googleapis.auth.oauth2.GoogleIdToken;
import com.google.api.client.googleapis.auth.oauth2.GoogleIdTokenVerifier;
import io.github.tony8864.login.application.exception.IdentityVerificationException;
import io.github.tony8864.login.application.port.IdentityVerifier;
import io.github.tony8864.login.domain.AuthProvider;
import io.github.tony8864.login.domain.ExternalIdentity;
import io.github.tony8864.login.domain.ProviderUserId;

import java.io.IOException;
import java.security.GeneralSecurityException;

public class GoogleIdentityVerifier implements IdentityVerifier {

    private final GoogleIdTokenVerifier verifier;

    public GoogleIdentityVerifier(GoogleIdTokenVerifier verifier) {
        this.verifier = verifier;
    }

    @Override
    public ExternalIdentity verify(String idToken) {

        GoogleIdToken googleIdToken = verifyToken(idToken);

        if (googleIdToken == null) {
            throw new IdentityVerificationException("Invalid Google Id token");
        }

        String subject = googleIdToken.getPayload().getSubject();

        return new ExternalIdentity(
                AuthProvider.GOOGLE,
                new ProviderUserId(subject)
        );
    }

    private GoogleIdToken verifyToken(String idToken) {
        try {
            return verifier.verify(idToken);
        } catch (IOException | GeneralSecurityException e) {
            throw new IdentityVerificationException("Failed to verify Google ID token", e);
        }
    }
}
