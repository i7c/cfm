package org.rliz.mbs.recording.boundary.impl;

import org.rliz.mbs.common.exception.ErrorCodes;
import org.rliz.mbs.common.exception.MbEntityNotFoundException;
import org.rliz.mbs.rating.model.Rated;
import org.rliz.mbs.rating.service.RatingService;
import org.rliz.mbs.recording.boundary.RecordingBoundaryService;
import org.rliz.mbs.recording.model.Recording;
import org.rliz.mbs.recording.repository.RecordingRepository;
import org.rliz.mbs.release.boundary.ReleaseBoundaryService;
import org.rliz.mbs.release.model.Release;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

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
    public Recording identifyRecording(Release release, String title) {
        List<Recording> recordings = recordingRepository.findAllByReleaseGroup(release.getReleaseGroup());

        List<Rated<Recording>> ratedRecordings = ratingService.rateRecordings(recordings, title);
        Rated<Recording> candidate = ratedRecordings.get(0);
        if (candidate.getRating() >= 75) {
            return candidate.getIt();
        } else {
            throw new MbEntityNotFoundException("Recording not found.", ErrorCodes.EC_001);
        }
    }

    @Override
    public Recording findByIdentifier(UUID identifier) {
        Recording foundRecording = recordingRepository.findByIdentifier(identifier);
        if (foundRecording == null) {
            throw new MbEntityNotFoundException(String.format("No recording for identifier %s.", identifier),
                    ErrorCodes.EC_001);
        }
        return foundRecording;
    }
}

