package io.github.tony8864.search.application;

import io.github.tony8864.search.application.exception.InvalidSearchQueryException;
import io.github.tony8864.search.application.model.PublicClipSummary;
import io.github.tony8864.search.application.model.SearchPublicClipsCommand;
import io.github.tony8864.search.application.model.SearchPublicClipsResult;
import io.github.tony8864.search.application.port.PublicClipSearch;

import java.util.List;

public class SearchPublicClipsService {

    private static final int MAX_RESULTS = 20;

    private final PublicClipSearch publicClipSearch;

    public SearchPublicClipsService(PublicClipSearch publicClipSearch) {
        this.publicClipSearch = publicClipSearch;
    }

    public SearchPublicClipsResult search(SearchPublicClipsCommand command) {
        String query = command.query();

        if (query == null || query.isBlank()) {
            throw new InvalidSearchQueryException("The query provided is invalid");
        }

        String normalizedQuery = query.trim();

        List<PublicClipSummary> clipSummaries = publicClipSearch.search(normalizedQuery, MAX_RESULTS);

        return new SearchPublicClipsResult(clipSummaries);
    }
}
