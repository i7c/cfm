package org.rliz.mbs.playback.boundary;

import org.rliz.mbs.recording.model.Recording;
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
     * @param artists list of artist names
     * @param title   title
     * @param release name of the release
     * @return pair of release and recording
     */
    Pair<Release, Recording> identifyPlayback(List<String> artists, String title, String release);
}
