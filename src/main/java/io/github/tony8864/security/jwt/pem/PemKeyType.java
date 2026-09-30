package io.github.tony8864.security.jwt.pem;

public enum PemKeyType {
    PUBLIC_KEY("public"),
    PRIVATE_KEY("private");

    private final String keyIdentifier;

    PemKeyType(String keyIdentifier) {
        this.keyIdentifier = keyIdentifier;
    }

    public String getKeyIdentifier() {
        return keyIdentifier;
    }
}
