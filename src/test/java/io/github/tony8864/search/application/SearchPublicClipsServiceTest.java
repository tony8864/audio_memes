package io.github.tony8864.search.application;

import io.github.tony8864.search.application.exception.InvalidSearchQueryException;
import io.github.tony8864.search.application.model.PlaybackReference;
import io.github.tony8864.search.application.model.PublicClipSummary;
import io.github.tony8864.search.application.model.SearchPublicClipsCommand;
import io.github.tony8864.search.application.model.SearchPublicClipsResult;
import io.github.tony8864.search.application.port.PublicClipSearch;
import io.github.tony8864.search.domain.ClipId;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.net.URI;
import java.time.Duration;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class SearchPublicClipsServiceTest {

    @Mock
    private PublicClipSearch publicClipSearch;

    @InjectMocks
    private SearchPublicClipsService service;

    private PublicClipSummary createSummary(
            String title,
            String playbackUri
    ) {
        return new PublicClipSummary(
                ClipId.newId(),
                title,
                null,
                null,
                "Audio Memes",
                Duration.ofSeconds(2),
                new PlaybackReference(URI.create(playbackUri))
        );
    }

    @Test
    void search_nullQuery_throwsInvalidSearchQueryExceptionWithoutCallingPort() {
        // arrange
        SearchPublicClipsCommand command = new SearchPublicClipsCommand(null);

        // assert
        assertThrows(InvalidSearchQueryException.class, () -> service.search(command));
    }


    @Test
    void search_blankQuery_throwsInvalidSearchQueryExceptionWithoutCallingPort() {
        // arrange
        SearchPublicClipsCommand command = new SearchPublicClipsCommand("");

        // assert
        assertThrows(InvalidSearchQueryException.class, () -> service.search(command));
    }

    @Test
    void search_queryWithSurroundingWhitespace_callsPortWithTrimmedQuery() {
        // arrange
        when(publicClipSearch.search("no god", 20)).thenReturn(List.of());
        SearchPublicClipsCommand command = new SearchPublicClipsCommand("   no god   ");

        // act
        service.search(command);

        // assert
        verify(publicClipSearch).search("no god", 20);
    }

    @Test
    void search_validQuery_callsPortWithMaximumOfTwentyResults() {
        // arrange
        when(publicClipSearch.search(anyString(), anyInt())).thenReturn(List.of());
        SearchPublicClipsCommand command = new SearchPublicClipsCommand("bruh");

        // act
        service.search(command);

        // assert
        verify(publicClipSearch).search("bruh", 20);
    }

    @Test
    void search_portReturnsMatches_returnsMatchesInSameOrder() {
        PublicClipSummary first = createSummary(
                "No God Please No",
                "https://audio.example/no-god.mp3"
        );

        PublicClipSummary second = createSummary(
                "Bruh",
                "https://audio.example/bruh.mp3"
        );

        List<PublicClipSummary> portMatches =
                List.of(first, second);

        when(publicClipSearch.search("reaction", 20))
                .thenReturn(portMatches);

        SearchPublicClipsResult result = service.search(
                new SearchPublicClipsCommand("reaction")
        );

        assertEquals(portMatches, result.clipSummaries());
    }
}