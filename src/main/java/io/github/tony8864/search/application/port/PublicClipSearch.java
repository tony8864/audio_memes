package io.github.tony8864.search.application.port;

import io.github.tony8864.search.application.model.PublicClipSummary;

import java.util.List;

public interface PublicClipSearch {
    List<PublicClipSummary> search(String query, int maxResults);
}
