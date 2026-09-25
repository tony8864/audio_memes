package io.github.tony8864.login.infrastructure.jwt;

import java.nio.file.Path;

public final class PemSourceFactory {

    private PemSourceFactory() {}

    public static PemSource getSource(PemKeyType keyType) {
        String key = keyType.getKeyIdentifier();
        String keyPath = "secrets/" + key + ".pem";
        return new FileSystemPemSource(Path.of(keyPath));
    }
}
