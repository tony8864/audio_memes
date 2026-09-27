package io.github.tony8864.login.infrastructure.verifier.google;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "auth.google")
public record GoogleVerifierProperties(
        String clientId
) {
}
