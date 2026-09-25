package io.github.tony8864.login.infrastructure.jwt;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Path;

public class FileSystemPemSource implements PemSource {

    private final Path path;

    public FileSystemPemSource(Path path) {
        this.path = path;
    }

    @Override
    public InputStream getInputStream() throws IOException {
        return new FileInputStream(path.toFile());
    }
}
