package org.rliz.mbs.recording.boundary.impl;

import org.rliz.mbs.common.exception.MbEntityNotFoundException;
import org.rliz.mbs.common.exception.MbLookupException;
import org.rliz.mbs.rating.model.Rated;
import org.rliz.mbs.rating.service.RatingService;
import org.rliz.mbs.recording.boundary.RecordingBoundaryService;
import org.rliz.mbs.recording.model.Recording;
import org.rliz.mbs.recording.repository.RecordingRepository;
import org.rliz.mbs.release.model.Release;
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
 * Implementation of {@link RecordingBoundaryService}.
 */
@Service
public class RecordingBoundaryServiceImpl implements RecordingBoundaryService {

    private RecordingRepository recordingRepository;

    private RatingService ratingService;

    @Autowired
    public RecordingBoundaryServiceImpl(RecordingRepository recordingRepository,
                                        RatingService ratingService) {
        this.recordingRepository = recordingRepository;
        this.ratingService = ratingService;
    }

    @Override
    public Recording identifyRecording(Release release, String title, int threshold) {
        List<Recording> recordings = recordingRepository.findAllByReleaseGroup(release.getReleaseGroup());

        if (CollectionUtils.isEmpty(recordings)) {
            throw new MbLookupException(MbLookupException.EC_NO_RESULTS,
                    "No recordings found for release with UUID %s.", release.getIdentifier());
        }

        List<Rated<Recording>> ratedRecordings = ratingService.rateRecordings(recordings, title);
        Rated<Recording> candidate = ratedRecordings.get(0);
        if (candidate.getRating() >= threshold) {
            return candidate.getIt();
        } else {
            throw new MbLookupException(MbLookupException.EC_LOW_RATING, "All found recordings were rated below " +
                    "threshold.");
        }
    }

    @Override
    public Recording findByIdentifier(UUID identifier) {
        Recording foundRecording = recordingRepository.findByIdentifier(identifier);
        if (foundRecording == null) {
            throw new MbEntityNotFoundException(MbEntityNotFoundException.EC_NO_SUCH_UUID,
                    "No recording found for UUID %s.", identifier);
        }
        return foundRecording;
    }

    @Override
    public Page<Recording> findRecordingByReleaseGroupIdentifierAndName(UUID releaseGroupId, String name,
                                                                        Pageable pageable) {
        List<Recording> foundRecordings = recordingRepository.findAllByReleaseGroupIdentifier(releaseGroupId);

        if (CollectionUtils.isEmpty(foundRecordings)) {
            throw new MbLookupException(MbLookupException.EC_NO_RESULTS,
                    "No recordings found for release group with UUID %s.", releaseGroupId);
        }

        List<Rated<Recording>> ratedRecordings = ratingService.rateRecordings(foundRecordings, name);
        List<Recording> recordings = ratedRecordings.stream().map(Rated::getIt).collect(Collectors.toList());
        if (pageable.getOffset() >= recordings.size()) {
            return new PageImpl<>(new ArrayList<>(), pageable, recordings.size());
        }
        int first = pageable.getOffset();
        int last = Math.min(first + pageable.getPageSize(), recordings.size());
        return new PageImpl<Recording>(recordings.subList(first, last), pageable, recordings.size());
    }
}

