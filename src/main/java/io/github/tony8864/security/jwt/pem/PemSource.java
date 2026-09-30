package io.github.tony8864.security.jwt.pem;

import java.io.IOException;
import java.io.InputStream;

public interface PemSource {
    InputStream getInputStream() throws IOException;
}
