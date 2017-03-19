package org.rliz.mbs.release.boundary.impl;

import org.rliz.mbs.common.exception.MbEntityNotFoundException;
import org.rliz.mbs.common.exception.MbLookupException;
import org.rliz.mbs.rating.model.Rated;
import org.rliz.mbs.rating.service.RatingService;
import org.rliz.mbs.release.boundary.ReleaseGroupBoundaryService;
import org.rliz.mbs.release.model.ReleaseGroup;
import org.rliz.mbs.release.repository.ReleaseGroupRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

/**
 * Implementation for {@link ReleaseGroupBoundaryService}.
 */
@Service
public class ReleaseGroupBoundaryServiceImpl implements ReleaseGroupBoundaryService {

    private ReleaseGroupRepository releaseGroupRepository;

    private RatingService ratingService;

    @Autowired
    public ReleaseGroupBoundaryServiceImpl(ReleaseGroupRepository releaseGroupRepository,
                                           RatingService ratingService) {
        this.releaseGroupRepository = releaseGroupRepository;
        this.ratingService = ratingService;
    }

    @Override
    public ReleaseGroup findByIdentifier(UUID identifier) {
        ReleaseGroup foundReleaseGroup = releaseGroupRepository.findByIdentifier(identifier);
        if (foundReleaseGroup == null) {
            throw new MbEntityNotFoundException(MbEntityNotFoundException.EC_NO_SUCH_UUID,
                    "No release group found for UUID %s.", identifier);
        }
        return foundReleaseGroup;
    }

    @Override
    public Page<ReleaseGroup> findByArtistsAndName(List<String> artists, String name, Pageable pageable) {
        List<ReleaseGroup> foundReleaseGroups = releaseGroupRepository
                .findReleaseGroupByArtistNames(artists.stream().map(String::toLowerCase).collect(Collectors.toList()));

        if (CollectionUtils.isEmpty(foundReleaseGroups)) {
            throw new MbLookupException(MbLookupException.EC_NO_RESULTS, "No release groups were found for artists %s",
                    String.join(", ", artists));
        }

        List<Rated<ReleaseGroup>> ratedReleaseGroups = ratingService.rateReleaseGroups(foundReleaseGroups, name);
        List<ReleaseGroup> releaseGroups = ratedReleaseGroups.stream()
                .map(Rated::getIt)
                .collect(Collectors.toList());
        if (pageable.getOffset() >= releaseGroups.size()) {
            return new PageImpl<ReleaseGroup>(new ArrayList<>(), pageable, releaseGroups.size());
        }
        int first = pageable.getOffset();
        int last = Math.min(first + pageable.getPageSize(), releaseGroups.size());
        return new PageImpl<ReleaseGroup>(releaseGroups.subList(first, last), pageable, releaseGroups.size());
    }
}
