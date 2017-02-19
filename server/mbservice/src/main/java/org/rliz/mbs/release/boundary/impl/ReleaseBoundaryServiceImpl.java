package org.rliz.mbs.release.boundary.impl;

import org.rliz.mbs.common.exception.MbLookupException;
import org.rliz.mbs.rating.model.Rated;
import org.rliz.mbs.rating.service.RatingService;
import org.rliz.mbs.release.boundary.ReleaseBoundaryService;
import org.rliz.mbs.release.model.Release;
import org.rliz.mbs.release.repository.ReleaseRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

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

        if (CollectionUtils.isEmpty(foundReleases)) {
            throw new MbLookupException(MbLookupException.EC_NO_RESULTS, "No releases were found for artists %s",
                    String.join(", ", artists));
        }

        List<Rated<Release>> ratedReleases = ratingService.rateReleases(foundReleases, title);
        Rated<Release> candidate = ratedReleases.get(0);

        if (candidate.getRating() > 75) {
            return candidate.getIt();
        } else {
            throw new MbLookupException(MbLookupException.EC_LOW_RATING, "All results were rated below threshold.");
        }
    }
}
