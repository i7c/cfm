package org.rliz.cfm.musicbrainz.api;

import com.google.common.collect.Lists;

import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Used to build query strings for musicbrainz' lucene.
 */
public class QueryStringBuilder {

    private List<String> artists;

    private List<String> titleParts;

    private List<String> albumParts;

    private QueryStringBuilder() {
        this.artists = Lists.newArrayList();
    }

    public static QueryStringBuilder queryString() {
        return new QueryStringBuilder();
    }

    public QueryStringBuilder withArtist(String artist) {
        for (String artistName : artist.split("feat\\.?")) {
            artists.add(artistName.trim());
        }
        return this;
    }

    public QueryStringBuilder withArtists(Collection<String> artists) {
        artists.stream().forEach(this::withArtist);
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
        List<String> artistMatches = artists.stream().map(artist -> String.format("artistname:\"%s\"", artist))
                .collect(Collectors.toList());
        String artistsString = String.join(" AND ", artistMatches);
        String titleString = String.format("recording:\"%s\"~%d", String.join(" ", titleParts), 2);
        String albumString = String.format("release:\"%s\"~%d", String.join(" ", albumParts), 2);

        return String.join(" AND ", artistsString, titleString, albumString);
    }
}
