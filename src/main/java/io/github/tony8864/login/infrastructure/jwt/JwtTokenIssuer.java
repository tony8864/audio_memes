package io.github.tony8864.login.infrastructure.jwt;

import com.nimbusds.jose.*;
import com.nimbusds.jose.crypto.RSASSASigner;
import com.nimbusds.jwt.JWTClaimsSet;
import com.nimbusds.jwt.SignedJWT;
import io.github.tony8864.login.application.exception.TokenIssuingException;
import io.github.tony8864.login.application.model.AccessToken;
import io.github.tony8864.login.application.port.TokenIssuer;
import io.github.tony8864.login.domain.User;

import java.security.interfaces.RSAPrivateKey;
import java.time.Clock;
import java.time.Instant;
import java.util.Date;

public class JwtTokenIssuer implements TokenIssuer {

    private final RSAPrivateKey privateKey;
    private final JwtProperties properties;
    private final Clock clock;

    public JwtTokenIssuer(RSAPrivateKey privateKey, JwtProperties properties, Clock clock) {
        this.privateKey = privateKey;
        this.properties = properties;
        this.clock = clock;
    }

    @Override
    public AccessToken issueFor(User user) throws TokenIssuingException {

        Instant issuedAt = clock.instant();
        Instant expiresAt = issuedAt.plus(properties.accessTokenLifetime());

        JWTClaimsSet payload = new JWTClaimsSet.Builder()
                .issuer(properties.issuer())
                .audience(properties.audience())
                .issueTime(Date.from(issuedAt))
                .expirationTime(Date.from(expiresAt))
                .subject(user.userId().value().toString())
                .build();

        JWSHeader header = new JWSHeader.Builder(JWSAlgorithm.RS256)
                .type(JOSEObjectType.JWT)
                .build();

        SignedJWT token = new SignedJWT(header, payload);
        JWSSigner signer = new RSASSASigner(privateKey);

        try {
            token.sign(signer);
        } catch (JOSEException e) {
            throw new TokenIssuingException("Failed to cryptographically sign the access token", e);
        }

        return new AccessToken(token.serialize());
    }
}
