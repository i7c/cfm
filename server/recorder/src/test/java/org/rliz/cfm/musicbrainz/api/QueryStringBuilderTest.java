package org.rliz.cfm.musicbrainz.api;

import org.junit.Test;

import static org.junit.Assert.assertEquals;

/**
 * Tests for the query string builder.
 */
public class QueryStringBuilderTest {

    @Test
    public void testSimpleArgs() {
        String queryString = QueryStringBuilder.queryString()
                .withArtist("Artist")
                .withTitle("Word1 Word2 - Word3")
                .withAlbum("X Y")
                .build();

        assertEquals(queryString,
                "artistname:\"Artist\" AND recording:\"Word1 Word2 - Word3\"~2 AND release:\"X Y\"~2");
    }
}
