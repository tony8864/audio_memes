package io.github.tony8864.search.application.model;

import java.net.URI;

public record PlaybackReference(URI uri) {
    public PlaybackReference {
        if (uri == null) {
            throw new IllegalArgumentException("Playback reference cannot be null");
        }
    }
}
