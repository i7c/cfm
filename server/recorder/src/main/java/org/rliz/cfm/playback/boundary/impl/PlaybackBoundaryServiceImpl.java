package org.rliz.cfm.playback.boundary.impl;

import org.rliz.cfm.common.exception.ApiArgumentsInvalidException;
import org.rliz.cfm.common.exception.ErrorCodes;
import org.rliz.cfm.common.security.SecurityContextHelper;
import org.rliz.cfm.musicbrainz.api.MusicbrainzRestClient;
import org.rliz.cfm.musicbrainz.api.QueryStringBuilder;
import org.rliz.cfm.musicbrainz.api.dto.MbRecordingDto;
import org.rliz.cfm.musicbrainz.api.dto.MbRecordingListDto;
import org.rliz.cfm.playback.api.dto.CreatePlaybackDto;
import org.rliz.cfm.playback.boundary.PlaybackBoundaryService;
import org.rliz.cfm.playback.model.Playback;
import org.rliz.cfm.playback.model.PlaybackOriginalData;
import org.rliz.cfm.playback.repository.PlaybackRepository;
import org.rliz.cfm.recording.boundary.RecordingBoundaryService;
import org.rliz.cfm.recording.model.Recording;
import org.rliz.cfm.release.boundary.ReleaseGroupBoundaryService;
import org.rliz.cfm.release.model.ReleaseGroup;
import org.rliz.cfm.user.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.time.Instant;
import java.util.*;

/**
 * Implementation of {@link PlaybackBoundaryServiceImpl}.
 */
@Service
public class PlaybackBoundaryServiceImpl implements PlaybackBoundaryService {

    private RecordingBoundaryService recordingBoundaryService;
    private ReleaseGroupBoundaryService releaseGroupBoundaryService;
    private PlaybackRepository playbackRepository;
    private MusicbrainzRestClient musicbrainzRestClient;

    /**
     * Constructor.
     *
     * @param recordingBoundaryService    recording service
     * @param releaseGroupBoundaryService release group service
     * @param playbackRepository          repo for playbacks
     * @param musicbrainzRestClient       REST client to query mb
     */
    @Autowired
    public PlaybackBoundaryServiceImpl(RecordingBoundaryService recordingBoundaryService,
                                       ReleaseGroupBoundaryService releaseGroupBoundaryService,
                                       PlaybackRepository playbackRepository, MusicbrainzRestClient
                                               musicbrainzRestClient) {
        this.recordingBoundaryService = recordingBoundaryService;
        this.releaseGroupBoundaryService = releaseGroupBoundaryService;
        this.playbackRepository = playbackRepository;
        this.musicbrainzRestClient = musicbrainzRestClient;
    }

    @Override
    public Playback createPlayback(CreatePlaybackDto dto) {
        User currentUser = SecurityContextHelper.getCurrentUser();

        Playback createdPlayback;
        if (Objects.nonNull(dto.getMbTrackId()) && Objects.nonNull(dto.getMbReleaseGroupId())) {
            createdPlayback = createPlaybackWithMbids(currentUser, dto.getMbTrackId(), dto.getMbReleaseGroupId());
        } else if (!CollectionUtils.isEmpty(dto.getArtists())
                && Objects.nonNull(dto.getTitle()) && Objects.nonNull(dto.getAlbum())) {
            createdPlayback = createPlaybackWithNames(currentUser, dto.getArtists(), dto.getTitle(), dto.getAlbum());
        } else {
            throw new ApiArgumentsInvalidException("Insufficient data to create a playback.", ErrorCodes.EC_002);
        }
        createdPlayback.setOriginalData(PlaybackOriginalData.fromCreatePlaybackDto(dto));
        createdPlayback.setIdentifier(UUID.randomUUID());
        return playbackRepository.save(createdPlayback);
    }

    private Playback createPlaybackWithMbids(User currentUser, UUID trackId, UUID releaseGroupId) {
        Recording recording = recordingBoundaryService.findOrCreateRecordingWithMbid(trackId);
        ReleaseGroup releaseGroup = releaseGroupBoundaryService.findOrCreateReleaseGroupWithMusicbrainz(releaseGroupId);
        Playback playback = new Playback(recording, releaseGroup, currentUser, Date.from(Instant.now()));
        return playback;
    }

    private Playback createPlaybackWithNames(User currentUser, List<String> artists, String title, String album) {
        String queryString = QueryStringBuilder.queryString()
                .withArtists(artists)
                .withTitle(title)
                .withAlbum(album)
                .build();
        MbRecordingListDto recordingListDto = musicbrainzRestClient.searchForRecordings(queryString,
                MusicbrainzRestClient.FORMAT_JSON);
        if (recordingListDto.getCount() < 1) {
            // create "broken" playback instead.
            return new Playback(null, null, currentUser, Date.from(Instant.now()));
        }
        MbRecordingDto recordingDto = recordingListDto.getRecordings().get(0);
        Recording recording = recordingBoundaryService
                .findOrCreateRecordingWithMbid(recordingDto.getMbid());

        ReleaseGroup releaseGroup = releaseGroupBoundaryService
                .findOrCreateReleaseGroupWithMusicbrainz(
                        recordingDto.getReleases().get(0).getReleaseGroupReference().getMbid());

        Playback playback = new Playback(recording, releaseGroup, currentUser, Date.from(Instant.now()));
        return playback;
    }

    @Override
    public Page<Playback> findAll(Pageable pageable) {
        return playbackRepository.findAll(pageable);
    }

    @Override
    public Page<Playback> findAllForCurrentUser(Pageable pageable) {
        User currentUser = SecurityContextHelper.getCurrentUser();
        return playbackRepository.findByUser(currentUser, pageable);
    }


}