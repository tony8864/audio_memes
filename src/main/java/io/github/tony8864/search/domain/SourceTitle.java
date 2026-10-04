package io.github.tony8864.search.domain;

public record SourceTitle(String value) {
    public SourceTitle {
        if (value == null || value.isBlank()) {
            throw new IllegalArgumentException("Source title cannot be blank");
        }
    }
}
