package io.github.tony8864.security.jwt;

import io.github.tony8864.login.application.model.AccessToken;
import io.github.tony8864.login.domain.*;
import io.github.tony8864.security.jwt.pem.PemParser;
import io.github.tony8864.security.jwt.pem.PemSource;
import io.github.tony8864.security.jwt.pem.StringPemSource;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.jwt.JwtException;

import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.NoSuchAlgorithmException;
import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;
import java.time.Clock;
import java.time.Duration;
import java.time.Instant;
import java.time.ZoneOffset;
import java.util.Base64;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class JwtDecoderTest {

    private static String validPublicPem;
    private static String validPrivatePem;

    @BeforeAll
    static void initializePublicPem() throws NoSuchAlgorithmException {
        KeyPairGenerator kpg = KeyPairGenerator.getInstance("RSA");
        kpg.initialize(2048);
        KeyPair kp = kpg.generateKeyPair();

        String publicKey = Base64.getEncoder().encodeToString(kp.getPublic().getEncoded());
        String privateKey = Base64.getEncoder().encodeToString(kp.getPrivate().getEncoded());

        validPublicPem = "-----BEGIN PUBLIC KEY-----\n" + publicKey + "-----END PUBLIC KEY-----\n";
        validPrivatePem = "-----BEGIN PRIVATE KEY-----\n" + privateKey + "-----END PRIVATE KEY-----\n";
    }

    private RSAPrivateKey privateKey;
    private RSAPublicKey publicKey;
    private JwtProperties properties;
    private JwtDecoder decoder;

    @BeforeEach
    void init() throws Exception {
        PemSource privateSource = new StringPemSource(validPrivatePem);
        PemSource publicSource = new StringPemSource(validPublicPem);

        privateKey = PemParser.parsePrivateKey(privateSource);
        publicKey = PemParser.parsePublicKey(publicSource);

        properties = new JwtProperties(
                "audio-playback-api",
                "audio-playback-api",
                Duration.ofMinutes(15)
        );

        decoder = new JwtConfiguration().jwtDecoder(publicKey, properties);
    }

    private User newUser(UserId userId) {
        ExternalIdentity externalIdentity =
                new ExternalIdentity(AuthProvider.GOOGLE, new ProviderUserId("google-id"));

        return new User(userId, externalIdentity, Instant.now());
    }

    @Test
    void decode_validToken_returnsJwtWithExpectedClaims() {
        // arrange
        JwtTokenIssuer issuer = new JwtTokenIssuer(
                privateKey,
                properties,
                Clock.systemUTC()
        );

        UserId userId = UserId.newId();
        AccessToken accessToken = issuer.issueFor(newUser(userId));

        // act
        Jwt decoded = decoder.decode(accessToken.value());

        // assert
        assertEquals(userId.value().toString(), decoded.getSubject());
    }

    @Test
    void decode_tokenSignedWithDifferentKey_throwsJwtException() throws NoSuchAlgorithmException {
        // arrange
        KeyPairGenerator kpg = KeyPairGenerator.getInstance("RSA");

        kpg.initialize(2048);

        RSAPrivateKey differentPrivateKey = (RSAPrivateKey) kpg.generateKeyPair().getPrivate();

        JwtTokenIssuer issuer = new JwtTokenIssuer(
                differentPrivateKey,
                properties,
                Clock.systemUTC()
        );

        AccessToken accessToken = issuer.issueFor(newUser(UserId.newId()));

        // act & assert
        assertThrows(JwtException.class, () -> decoder.decode(accessToken.value()));
    }

    @Test
    void decode_tokenWithInvalidIssuer_throwsJwtException() {
        // arrange
        JwtProperties differentProperties = new JwtProperties(
                "diff-issuer",
                properties.audience(),
                properties.accessTokenLifetime()
        );

        JwtTokenIssuer issuer = new JwtTokenIssuer(
                privateKey,
                differentProperties,
                Clock.systemUTC()
        );

        AccessToken accessToken = issuer.issueFor(newUser(UserId.newId()));

        // act & assert
        assertThrows(JwtException.class, () -> decoder.decode(accessToken.value()));
    }

    @Test
    void decode_tokenWithInvalidAudience_throwsJwtException() {
        // arrange
        JwtProperties differentProperties = new JwtProperties(
                properties.issuer(),
                "diff-aud",
                properties.accessTokenLifetime()
        );

        JwtTokenIssuer issuer = new JwtTokenIssuer(
                privateKey,
                differentProperties,
                Clock.systemUTC()
        );

        AccessToken accessToken = issuer.issueFor(newUser(UserId.newId()));

        // act & assert
        assertThrows(JwtException.class, () -> decoder.decode(accessToken.value()));
    }

    @Test
    void decode_expiredToken_throwsJwtException() {
        // arrange
        Instant issuedAt = Instant.now()
                .minus(properties.accessTokenLifetime())
                .minus(Duration.ofMinutes(5));

        Clock pastClock = Clock.fixed(issuedAt, ZoneOffset.UTC);

        JwtTokenIssuer issuer = new JwtTokenIssuer(
                privateKey,
                properties,
                pastClock
        );

        AccessToken accessToken = issuer.issueFor(newUser(UserId.newId()));

        // act & assert
        assertThrows(JwtException.class, () -> decoder.decode(accessToken.value()));
    }
}
