package org.rliz.mbs.recording.boundary;

import org.rliz.mbs.common.exception.MbEntityNotFoundException;
import org.rliz.mbs.common.exception.MbLookupException;
import org.rliz.mbs.rating.model.Rated;
import org.rliz.mbs.rating.service.RatingService;
import org.rliz.mbs.recording.data.Recording;
import org.rliz.mbs.recording.data.RecordingRepo;
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

@Service
public class RecordingBoundaryService {

    @Autowired
    private RecordingRepo recordingRepo;

    @Autowired
    private RatingService ratingService;

    public Recording identifyRecording(Release release, String title, int threshold) {
        List<Recording> recordings = recordingRepo.findAllByReleaseGroup(release.getReleaseGroup());

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

    public Recording findByIdentifier(UUID identifier) {
        Recording foundRecording = recordingRepo.findByIdentifier(identifier);
        if (foundRecording == null) {
            throw new MbEntityNotFoundException(MbEntityNotFoundException.EC_NO_SUCH_UUID,
                    "No recording found for UUID %s.", identifier);
        }
        return foundRecording;
    }

    public Page<Recording> findRecordingByReleaseGroupIdentifierAndName(UUID releaseGroupId, String name,
                                                                        Pageable pageable) {
        List<Recording> foundRecordings = recordingRepo.findAllByReleaseGroupIdentifier(releaseGroupId);

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

