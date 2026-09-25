package io.github.tony8864.login.infrastructure.jwt;

import com.nimbusds.jose.JOSEObjectType;
import com.nimbusds.jose.JWSAlgorithm;
import com.nimbusds.jose.JWSHeader;
import com.nimbusds.jose.JWSVerifier;
import com.nimbusds.jose.crypto.RSASSAVerifier;
import com.nimbusds.jwt.JWTClaimsSet;
import com.nimbusds.jwt.SignedJWT;
import io.github.tony8864.login.application.model.AccessToken;
import io.github.tony8864.login.domain.*;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.NoSuchAlgorithmException;
import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;
import java.text.ParseException;
import java.time.Clock;
import java.time.Duration;
import java.time.Instant;
import java.time.ZoneOffset;
import java.time.temporal.ChronoUnit;
import java.util.Base64;
import java.util.Date;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class JwtTokenIssuerTest {

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

    @BeforeEach
    void init() throws Exception {
        PemSource pemSource = new StringPemSource(validPrivatePem);
        privateKey = PemParser.parsePrivateKey(pemSource);
    }

    private User newUser(UserId userId) {
        ExternalIdentity externalIdentity = new ExternalIdentity(
                AuthProvider.GOOGLE,
                new ProviderUserId("provider-id")
        );

        return new User(userId, externalIdentity, Instant.now());
    }

    @Test
    void issueFor_validUser_returnsTokenWithExpectedClaims() throws ParseException {
        // arrange
        JwtProperties properties = new JwtProperties(
                "audio-playback-api",
                "audio-playback-api",
                Duration.of(15, ChronoUnit.MINUTES)
        );

        Instant fixedInstant = Instant.parse("2026-09-25T12:00:00Z");

        Clock clock = Clock.fixed(
                fixedInstant,
                ZoneOffset.UTC
        );

        UserId userId = UserId.newId();
        JwtTokenIssuer issuer = new JwtTokenIssuer(privateKey, properties, clock);

        // act
        AccessToken accessToken = issuer.issueFor(newUser(userId));

        // assert
        SignedJWT parsedToken = SignedJWT.parse(accessToken.value());
        JWTClaimsSet claims = parsedToken.getJWTClaimsSet();

        assertEquals("audio-playback-api", claims.getIssuer());
        assertEquals("audio-playback-api", claims.getAudience().get(0));
        assertEquals(userId.value().toString(), claims.getSubject());
        assertEquals(Date.from(fixedInstant), claims.getIssueTime());
        assertEquals(
                Date.from(fixedInstant.plus(properties.accessTokenLifetime())),
                claims.getExpirationTime());
    }

    @Test
    void issueFor_validUser_returnsTokenWithExpectedHeader() throws ParseException {
        // arrange
        JwtProperties properties = new JwtProperties(
                "audio-playback-api",
                "audio-playback-api",
                Duration.of(15, ChronoUnit.MINUTES)
        );

        Instant fixedInstant = Instant.parse("2026-09-25T12:00:00Z");

        Clock clock = Clock.fixed(
                fixedInstant,
                ZoneOffset.UTC
        );

        UserId userId = UserId.newId();
        JwtTokenIssuer issuer = new JwtTokenIssuer(privateKey, properties, clock);

        // act
        AccessToken accessToken = issuer.issueFor(newUser(userId));

        // assert
        SignedJWT parsedToken = SignedJWT.parse(accessToken.value());
        JWSHeader header = parsedToken.getHeader();

        assertEquals(JWSAlgorithm.RS256, header.getAlgorithm());
        assertEquals(JOSEObjectType.JWT, header.getType());
    }

    @Test
    void issueFor_validUser_returnsTokenSignedWithConfiguredPrivateKey() throws Exception {
        // arrange
        JwtProperties properties = new JwtProperties(
                "audio-playback-api",
                "audio-playback-api",
                Duration.of(15, ChronoUnit.MINUTES)
        );

        Instant fixedInstant = Instant.parse("2026-09-25T12:00:00Z");

        Clock clock = Clock.fixed(
                fixedInstant,
                ZoneOffset.UTC
        );

        UserId userId = UserId.newId();
        JwtTokenIssuer issuer = new JwtTokenIssuer(privateKey, properties, clock);

        // act
        AccessToken accessToken = issuer.issueFor(newUser(userId));

        // assert
        SignedJWT parsedToken = SignedJWT.parse(accessToken.value());
        RSAPublicKey publicKey = PemParser.parsePublicKey(new StringPemSource(validPublicPem));
        JWSVerifier verifier = new RSASSAVerifier(publicKey);
        assertTrue(parsedToken.verify(verifier));
    }
}