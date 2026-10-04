package io.github.tony8864.search.domain;

import java.util.UUID;

public record AudioId(UUID value) {
    public AudioId {
        if (value == null) {
            throw new IllegalArgumentException("Audio ID cannto be null");
        }
    }

    public static AudioId newId() {
        return new AudioId(UUID.randomUUID());
    }
}
