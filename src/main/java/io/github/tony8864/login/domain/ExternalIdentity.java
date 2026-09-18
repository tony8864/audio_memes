package io.github.tony8864.login.domain;

public record ExternalIdentity(
        AuthProvider provider,
        ProviderUserId providerUserId
) {
}
