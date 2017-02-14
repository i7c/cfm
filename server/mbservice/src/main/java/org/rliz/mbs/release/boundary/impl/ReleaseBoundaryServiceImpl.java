package org.rliz.mbs.release.boundary.impl;

import org.rliz.mbs.common.exception.ErrorCodes;
import org.rliz.mbs.common.exception.MbEntityNotFoundException;
import org.rliz.mbs.rating.model.Rated;
import org.rliz.mbs.rating.service.RatingService;
import org.rliz.mbs.release.boundary.ReleaseBoundaryService;
import org.rliz.mbs.release.model.Release;
import org.rliz.mbs.release.repository.ReleaseRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Implementation for {@link ReleaseBoundaryService}.
 */
@Service
public class ReleaseBoundaryServiceImpl implements ReleaseBoundaryService {

    private ReleaseRepository releaseRepository;

    private RatingService ratingService;

    @Autowired
    public ReleaseBoundaryServiceImpl(ReleaseRepository releaseRepository, RatingService ratingService) {
        this.releaseRepository = releaseRepository;
        this.ratingService = ratingService;
    }

    @Override
    public Release identifyRelease(List<String> artists, String title) {
        List<Release> foundReleases = releaseRepository.findReleaseByArtistNames(artists);

        List<Rated<Release>> ratedReleases = ratingService.rateReleases(foundReleases, title);
        Rated<Release> candidate = ratedReleases.get(0);

        if (candidate.getRating() > 75) {
            return candidate.getIt();
        } else {
            throw new MbEntityNotFoundException("Could not find matching release.", ErrorCodes.EC_001);
        }
    }
}
