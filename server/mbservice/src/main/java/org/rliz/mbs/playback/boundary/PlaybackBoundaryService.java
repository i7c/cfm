package org.rliz.mbs.playback.boundary;

import org.rliz.mbs.recording.data.Recording;
import org.rliz.mbs.release.model.Release;
import org.springframework.data.util.Pair;

import java.util.List;

/**
 * Service for playback related stuff.
 */
public interface PlaybackBoundaryService {

    /**
     * Try to identify {@link Release} and {@link Recording} by given details.
     *
     * @param artists  list of artist names
     * @param title    title
     * @param release  name of the release
     * @param rgthres  min score to match a release group
     * @param recthres min score to match a recording
     * @return pair of release and recording
     */
    Pair<Release, Recording> identifyPlayback(List<String> artists, String title, String release, int rgthres,
                                              int recthres);
}
