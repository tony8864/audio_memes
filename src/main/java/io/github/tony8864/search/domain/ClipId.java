package io.github.tony8864.search.domain;

import java.util.UUID;

public record ClipId(UUID uuid) {
    public ClipId {
        if (uuid == null) {
            throw new IllegalArgumentException("Clip Id cannot be null");
        }
    }

    public static ClipId newId() {
        return new ClipId(UUID.randomUUID());
    }
}