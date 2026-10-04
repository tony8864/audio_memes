package io.github.tony8864.search.application.model;

import io.github.tony8864.search.domain.ClipId;

import java.time.Duration;

public record PublicClipSummary(
        ClipId clipId,
        String title,
        String sourceTitle,
        String speaker,
        String creator,
        Duration duration,
        PlaybackReference playbackReference
) {
}
