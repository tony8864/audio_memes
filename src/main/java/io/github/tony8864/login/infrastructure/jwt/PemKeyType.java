package io.github.tony8864.login.infrastructure.jwt;

public enum PemKeyType {
    PUBLIC_KEY("public_key"),
    PRIVATE_KEY("private_key");

    private final String keyIdentifier;

    PemKeyType(String keyIdentifier) {
        this.keyIdentifier = keyIdentifier;
    }

    public String getKeyIdentifier() {
        return keyIdentifier;
    }
}
