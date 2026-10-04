package io.github.tony8864.search.application.model;

import java.util.List;

public record SearchPublicClipsResult(List<PublicClipSummary> clipSummaries) {
    public SearchPublicClipsResult {
        clipSummaries = List.copyOf(clipSummaries);
    }
}
