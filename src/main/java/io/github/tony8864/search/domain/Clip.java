package io.github.tony8864.search.domain;

import java.time.Duration;
import java.util.List;

public class Clip {
    private final ClipId clipId;
    private final ClipCreator creator;
    private final AudioId audioId;
    private final ClipTitle title;
    private final SourceTitle sourceTitle;
    private final Speaker speaker;
    private final List<String> aliases;
    private final List<String> tags;
    private final Duration duration;
    private PublicationStatus status;

    private Clip(
            ClipId clipId,
            ClipCreator creator,
            AudioId audioId,
            ClipTitle title,
            SourceTitle sourceTitle,
            Speaker speaker,
            Duration duration,
            PublicationStatus status,
            List<String> aliases,
            List<String> tags
    ) {

        if (clipId == null) {
            throw new IllegalArgumentException("Clip ID is required, cannot be null");
        }

        if (creator == null) {
            throw new IllegalArgumentException("Creator is required, cannot be null");
        }

        if (audioId == null) {
            throw new IllegalArgumentException("Audio ID is required, cannot be null");
        }

        if (title == null) {
            throw new IllegalArgumentException("Clip title is required, cannot be null");
        }

        if (aliases == null) {
            throw new IllegalArgumentException("Aliases are required, cannot be null");
        }

        if (tags == null) {
            throw new IllegalArgumentException("Tags are required, cannot be null");
        }

        if (duration == null) {
            throw new IllegalArgumentException("Clip duration cannot be null");
        }

        if (status == null) {
            throw new IllegalArgumentException("Clip status cannot be null");
        }

        this.clipId = clipId;
        this.creator = creator;
        this.audioId = audioId;
        this.title = title;
        this.sourceTitle = sourceTitle;
        this.speaker = speaker;
        this.duration = duration;
        this.aliases = List.copyOf(aliases);
        this.tags = List.copyOf(tags);
        this.status = status;
    }
}