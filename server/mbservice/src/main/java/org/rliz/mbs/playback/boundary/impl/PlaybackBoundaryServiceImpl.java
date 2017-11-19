package org.rliz.mbs.playback.boundary.impl;

import org.rliz.mbs.playback.boundary.PlaybackBoundaryService;
import org.rliz.mbs.recording.boundary.RecordingBoundaryService;
import org.rliz.mbs.recording.data.Recording;
import org.rliz.mbs.release.boundary.ReleaseBoundaryService;
import org.rliz.mbs.release.model.Release;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.util.Pair;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Implementation for {@link PlaybackBoundaryService}.
 */
@Service
public class PlaybackBoundaryServiceImpl implements PlaybackBoundaryService {

    private ReleaseBoundaryService releaseBoundaryService;

    private RecordingBoundaryService recordingBoundaryService;

    @Autowired
    public PlaybackBoundaryServiceImpl(ReleaseBoundaryService releaseBoundaryService, RecordingBoundaryService
            recordingBoundaryService) {
        this.releaseBoundaryService = releaseBoundaryService;
        this.recordingBoundaryService = recordingBoundaryService;
    }

    @Override
    public Pair<Release, Recording> identifyPlayback(List<String> artists, String title, String release, int rgthres,
                                                     int recthres) {
        Release identifiedRelease = releaseBoundaryService.identifyRelease(artists, release, rgthres);
        Recording identifiedRecording = recordingBoundaryService.identifyRecording(identifiedRelease, title, recthres);
        return Pair.of(identifiedRelease, identifiedRecording);
    }
}
