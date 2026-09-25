package io.github.tony8864.login.infrastructure.jwt;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;

public class StringPemSource implements PemSource {

    private final String pemContent;

    public StringPemSource(String pemContent) {
        this.pemContent = pemContent;
    }

    @Override
    public InputStream getInputStream() throws IOException {
        return new ByteArrayInputStream(pemContent.getBytes(StandardCharsets.UTF_8));
    }
}
