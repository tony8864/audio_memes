package io.github.tony8864.login.infrastructure.jwt;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.NoSuchAlgorithmException;
import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;
import java.util.Base64;

import static org.junit.jupiter.api.Assertions.*;

class PemParserTest {

    private static String validPublicPem;
    private static String validPrivatePem;

    @BeforeAll
    static void generateValidTestData() throws NoSuchAlgorithmException {
        KeyPairGenerator kpg = KeyPairGenerator.getInstance("RSA");
        kpg.initialize(2048);
        KeyPair kp = kpg.generateKeyPair();

        String publicKey = Base64.getEncoder().encodeToString(kp.getPublic().getEncoded());
        String privateKey = Base64.getEncoder().encodeToString(kp.getPrivate().getEncoded());

        validPublicPem = "-----BEGIN PUBLIC KEY-----\n" + publicKey + "-----END PUBLIC KEY-----\n";
        validPrivatePem = "-----BEGIN PRIVATE KEY-----\n" + privateKey + "-----END PRIVATE KEY-----\n";
    }

    @Test
    void parsePublicKey_validPublicKey_returnsParsedPublicKey() throws Exception {
        // arrange
        PemSource source = new StringPemSource(validPublicPem);

        // act
        RSAPublicKey publicKey = PemParser.parsePublicKey(source);

        // assert
        assertNotNull(publicKey);
        assertEquals("RSA", publicKey.getAlgorithm());
        assertEquals(2048, publicKey.getModulus().bitLength());
    }

    @Test
    void parsePrivateKey_validPrivateKey_returnsParsedPrivateKey() throws Exception {
        // arrange
        PemSource source = new StringPemSource(validPrivatePem);

        // act
        RSAPrivateKey privateKey = PemParser.parsePrivateKey(source);

        // assert
        assertNotNull(privateKey);
        assertEquals("RSA", privateKey.getAlgorithm());
        assertEquals(2048, privateKey.getModulus().bitLength());
    }

    @Test
    void parsePublicKey_missingHeader_throwsIllegalArgumentException() throws Exception {
        // arrange
        String brokenPem = validPublicPem.replace("-----BEGIN PUBLIC KEY-----", "");
        PemSource source = new StringPemSource(brokenPem);

        // act
        IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                () -> PemParser.parsePublicKey(source)
        );

        // assert
        assertTrue(exception.getMessage().contains("Invalid PEM format for public key"));
    }

    @Test
    void parsePrivateKey_missingHeader_throwsIllegalArgumentException() throws Exception {
        // arrange
        String brokenPem = validPrivatePem.replace("-----BEGIN PRIVATE KEY-----", "");
        PemSource source = new StringPemSource(brokenPem);

        // act
        IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                () -> PemParser.parsePrivateKey(source)
        );

        // assert
        assertTrue(exception.getMessage().contains("Invalid PEM format for private key"));
    }

    @Test
    void parsePublicKey_missingFooter_throwsIllegalArgumentException() throws Exception {
        // arrange
        String brokenPem = validPublicPem.replace("-----END PUBLIC KEY-----", "");
        PemSource source = new StringPemSource(brokenPem);

        // act
        IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                () -> PemParser.parsePublicKey(source)
        );

        // assert
        assertTrue(exception.getMessage().contains("Invalid PEM format for public key"));
    }

    @Test
    void parsePrivateKey_missingFooter_throwsIllegalArgumentException() throws Exception {
        // arrange
        String brokenPem = validPrivatePem.replace("-----END PRIVATE KEY-----", "");
        PemSource source = new StringPemSource(brokenPem);

        // act
        IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                () -> PemParser.parsePrivateKey(source)
        );

        // assert
        assertTrue(exception.getMessage().contains("Invalid PEM format for private key"));
    }

    @Test
    void parsePublicKey_corruptedBase64Content_throwsException() {
        // arrange
        String corruptedPem = "-----BEGIN PUBLIC KEY-----\nNOT_VALID_BASE64_GARBAGE_DATA==\n-----END PUBLIC KEY-----";
        PemSource source = new StringPemSource(corruptedPem);

        // act & assert
        assertThrows(Exception.class, () -> PemParser.parsePublicKey(source));
    }

    @Test
    void parsePrivateKey_corruptedBase64Content_throwsException() {
        // arrange
        String corruptedPem = "-----BEGIN PRIVATE KEY-----\nNOT_VALID_BASE64_GARBAGE_DATA==\n-----END PRIVATE KEY-----";
        PemSource source = new StringPemSource(corruptedPem);

        // act & assert
        assertThrows(Exception.class, () -> PemParser.parsePrivateKey(source));
    }
}