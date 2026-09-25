package io.github.tony8864.login.infrastructure.jwt;

import java.time.Duration;

public record JwtProperties(
        String issuer,
        String audience,
        Duration accessTokenLifetime
) {
}
