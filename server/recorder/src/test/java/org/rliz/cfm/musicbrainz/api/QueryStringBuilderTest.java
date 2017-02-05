package org.rliz.cfm.musicbrainz.api;

import org.junit.Test;

import static org.junit.Assert.assertEquals;

/**
 * Tests for the query string builder.
 */
public class QueryStringBuilderTest {

    @Test
    public void simpleArgs() {
        String queryString = QueryStringBuilder.queryString()
                .withArtist("Artist")
                .withTitle("Word1 Word2 - Word3")
                .withAlbum("X Y")
                .build();

        assertEquals("artistname:\"Artist\" AND recording:\"Word1 Word2 - Word3\"~2 AND release:\"X Y\"~2",
                queryString);
    }

    @Test
    public void multipleArtists() {
        String queryString = QueryStringBuilder.queryString()
                .withArtist("Artist 1")
                .withArtist("Artist 2")
                .withTitle("Title title")
                .withAlbum("ALBUM")
                .build();

        assertEquals("artistname:\"Artist 1\" AND artistname:\"Artist 2\" AND recording:\"Title title\"~2 " +
                        "AND release:\"ALBUM\"~2",
                queryString);
    }
}
