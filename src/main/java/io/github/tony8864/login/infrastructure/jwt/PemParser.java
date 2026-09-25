package io.github.tony8864.login.infrastructure.jwt;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.security.KeyFactory;
import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.util.Base64;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class PemParser {

    private final static String PUBLIC_HEADER = "-----BEGIN PUBLIC KEY-----";
    private final static String PUBLIC_FOOTER = "-----END PUBLIC KEY-----";
    private final static String PRIVATE_HEADER = "-----BEGIN PRIVATE KEY-----";
    private final static String PRIVATE_FOOTER = "-----END PRIVATE KEY-----";

    static RSAPublicKey parsePublicKey(PemSource pemSource) throws Exception {
        try (InputStream in = pemSource.getInputStream();
             BufferedReader reader =
                     new BufferedReader(
                             new InputStreamReader(in, StandardCharsets.UTF_8))
        ) {

            Stream<String> lines = reader.lines();
            String content = lines.collect(Collectors.joining());

            if (!isValidPublicPem(content)) {
                throw new IllegalArgumentException("Invalid PEM format for public key");
            }

            String publicKey = content
                    .replace(PUBLIC_HEADER, "")
                    .replace(PUBLIC_FOOTER, "")
                    .replaceAll("\\s", "");

            byte[] keyBytes = Base64.getDecoder().decode(publicKey);
            X509EncodedKeySpec spec = new X509EncodedKeySpec(keyBytes);
            KeyFactory kf = KeyFactory.getInstance("RSA");
            return (RSAPublicKey) kf.generatePublic(spec);
        }
    }

    static RSAPrivateKey parsePrivateKey(PemSource pemSource) throws Exception {
        try (InputStream in = pemSource.getInputStream(); BufferedReader reader =
                new BufferedReader(
                        new InputStreamReader(in, StandardCharsets.UTF_8))
        ) {
            Stream<String> lines = reader.lines();
            String content = lines.collect(Collectors.joining());

            if (!isValidPrivatePem(content)) {
                throw new IllegalArgumentException("Invalid PEM format for private key");
            }

            String privateKey = content
                    .replace(PRIVATE_HEADER, "")
                    .replace(PRIVATE_FOOTER, "")
                    .replaceAll("\\s", "");

            byte[] keyBytes = Base64.getDecoder().decode(privateKey);
            PKCS8EncodedKeySpec spec = new PKCS8EncodedKeySpec(keyBytes);
            KeyFactory kf = KeyFactory.getInstance("RSA");
            return (RSAPrivateKey) kf.generatePrivate(spec);
        }
    }

    private static boolean isValidPublicPem(String content) {
        return content.contains(PUBLIC_HEADER) &&
                content.contains(PUBLIC_FOOTER);
    }

    private static boolean isValidPrivatePem(String content) {
        return content.contains(PRIVATE_HEADER) &&
                content.contains(PRIVATE_FOOTER);
    }
}
