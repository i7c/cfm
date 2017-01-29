package org.rliz.cfm.musicbrainz.api;

import com.google.common.collect.Lists;

import java.util.List;

/**
 * Used to build query strings for musicbrainz' lucene.
 */
public class QueryStringBuilder {

    private String artist;

    private List<String> titleParts;

    private List<String> albumParts;

    public static QueryStringBuilder queryString() {
        return new QueryStringBuilder();
    }

    public QueryStringBuilder withArtist(String artist) {
        this.artist = artist;
        return this;
    }

    public QueryStringBuilder withAlbum(String album) {
        this.albumParts = Lists.newArrayList(album.split("\\s+"));

        return this;
    }

    public QueryStringBuilder withTitle(String title) {

        this.titleParts = Lists.newArrayList(title.split("\\s+"));
        return this;
    }
    public String build() {
        String artistString = String.format("artistname:\"%s\"", artist);
        String titleString = String.format("recording:\"%s\"~%d", String.join(" ", titleParts), 2);
        String albumString = String.format("release:\"%s\"~%d", String.join(" ", albumParts), 2);

        return String.join(" AND ", artistString, titleString, albumString);
    }
}
