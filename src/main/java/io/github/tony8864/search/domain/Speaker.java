package io.github.tony8864.search.domain;

public record Speaker(String value) {
    public Speaker {
        if (value == null || value.isBlank()) {
            throw new IllegalArgumentException("Speaker cannot be blank");
        }
    }
}
