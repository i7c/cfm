package org.rliz.cfm.playback.boundary.impl;

import org.rliz.cfm.playback.boundary.PlaybackBoundaryService;
import org.rliz.cfm.playback.model.Playback;
import org.rliz.cfm.playback.repository.PlaybackRepository;
import org.rliz.cfm.recording.boundary.RecordingBoundaryService;
import org.rliz.cfm.recording.model.Recording;
import org.rliz.cfm.release.boundary.ReleaseGroupBoundaryService;
import org.rliz.cfm.release.model.ReleaseGroup;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.Date;
import java.util.UUID;

/**
 * Implementation of {@link PlaybackBoundaryServiceImpl}.
 */
@Service
public class PlaybackBoundaryServiceImpl implements PlaybackBoundaryService {

    private RecordingBoundaryService recordingBoundaryService;
    private ReleaseGroupBoundaryService releaseGroupBoundaryService;
    private PlaybackRepository playbackRepository;

    /**
     * Constructor.
     *
     * @param recordingBoundaryService    recording service
     * @param releaseGroupBoundaryService release group service
     * @param playbackRepository          repo for playbacks
     */
    @Autowired
    public PlaybackBoundaryServiceImpl(RecordingBoundaryService recordingBoundaryService, ReleaseGroupBoundaryService releaseGroupBoundaryService, PlaybackRepository playbackRepository) {
        this.recordingBoundaryService = recordingBoundaryService;
        this.releaseGroupBoundaryService = releaseGroupBoundaryService;
        this.playbackRepository = playbackRepository;
    }

    @Override
    public Playback createPlaybackWithMbids(UUID trackId, UUID releaseGroupId) {
        Recording recording = recordingBoundaryService.findOrCreateRecordingWithMusicbrainz(trackId);
        ReleaseGroup releaseGroup = releaseGroupBoundaryService.findOrCreateReleaseGroupWithMusicbrainz(releaseGroupId);
        Playback playback = new Playback(recording, releaseGroup, Date.from(Instant.now()));
        playback.setIdentifier(UUID.randomUUID());
        return playbackRepository.save(playback);
    }

    @Override
    public Page<Playback> findAll(Pageable pageable) {
        return playbackRepository.findAll(pageable);
    }


}
