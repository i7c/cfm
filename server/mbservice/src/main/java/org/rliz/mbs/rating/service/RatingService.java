package org.rliz.mbs.rating.service;

import org.rliz.mbs.rating.model.Rated;
import org.rliz.mbs.recording.model.Recording;
import org.rliz.mbs.release.model.Release;

import java.util.List;

/**
 * This service provides algorithms to rate entities against a search criterion.
 */
public interface RatingService {

    /**
     * Rates recordings using a given title.
     *
     * @param recordings the set of recordings to rate
     * @param title      the assumed "correct" title of the recording
     * @return a sorted list of rated recordings, best match is the first entry
     */
    List<Rated<Recording>> rateRecordings(List<Recording> recordings, String title);

    /**
     * Rates releases using a given title.
     *
     * @param releases the set of releases to rate
     * @param title    an assumed correct title of the release
     * @return a sorted list of releases, best match is the first entry
     */
    List<Rated<Release>> rateReleases(List<Release> releases, String title);

}
