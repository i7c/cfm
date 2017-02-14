package org.rliz.mbs.release.boundary;

import org.rliz.mbs.release.model.Release;

import java.util.List;

/**
 * Service related to {@link Release}s.
 */
public interface ReleaseBoundaryService {

    /**
     * Try to identify a release
     *
     * @param artists non-empty subset of the artists who are credited for the release
     * @param title   title of the release
     * @return the matching release
     */
    Release identifyRelease(List<String> artists, String title);

}
