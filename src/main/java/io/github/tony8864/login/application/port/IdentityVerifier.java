package io.github.tony8864.login.application.port;

import io.github.tony8864.login.domain.ExternalIdentity;

public interface IdentityVerifier {
    ExternalIdentity verify(String idToken);
}
