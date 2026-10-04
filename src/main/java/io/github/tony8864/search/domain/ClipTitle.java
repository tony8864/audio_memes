package io.github.tony8864.search.domain;

public record ClipTitle(String value) {
    public ClipTitle {
        if (value == null) {
            throw new IllegalArgumentException("Clip title cannot be null");
        }
    }
}
