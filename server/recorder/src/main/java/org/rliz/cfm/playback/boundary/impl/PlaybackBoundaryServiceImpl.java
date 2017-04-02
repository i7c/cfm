package org.rliz.cfm.playback.boundary.impl;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.rliz.cfm.common.exception.ApiArgumentsInvalidException;
import org.rliz.cfm.common.exception.EntityNotFoundException;
import org.rliz.cfm.common.exception.UnauthorizedException;
import org.rliz.cfm.common.security.SecurityContextHelper;
import org.rliz.cfm.mbs.dto.MbPlaybackDetailsDto;
import org.rliz.cfm.mbs.service.MbsRestClient;
import org.rliz.cfm.playback.api.dto.SavePlaybackDto;
import org.rliz.cfm.playback.boundary.PlaybackBoundaryService;
import org.rliz.cfm.playback.model.Playback;
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
import java.util.Date;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

/**
 * Implementation of {@link PlaybackBoundaryServiceImpl}.
 */
@Service
public class PlaybackBoundaryServiceImpl implements PlaybackBoundaryService {

    protected final Log logger = LogFactory.getLog(getClass());

    private RecordingBoundaryService recordingBoundaryService;
    private ReleaseGroupBoundaryService releaseGroupBoundaryService;
    private PlaybackRepository playbackRepository;
    private MbsRestClient mbsRestClient;

    /**
     * Constructor.
     *
     * @param recordingBoundaryService    recording service
     * @param releaseGroupBoundaryService release group service
     * @param playbackRepository          repo for playbacks
     * @param mbsRestClient               REST client to query MBS
     */
    @Autowired
    public PlaybackBoundaryServiceImpl(RecordingBoundaryService recordingBoundaryService,
                                       ReleaseGroupBoundaryService releaseGroupBoundaryService,
                                       PlaybackRepository playbackRepository,
                                       MbsRestClient mbsRestClient) {
        this.recordingBoundaryService = recordingBoundaryService;
        this.releaseGroupBoundaryService = releaseGroupBoundaryService;
        this.playbackRepository = playbackRepository;
        this.mbsRestClient = mbsRestClient;
    }

    @Override
    public Playback createPlayback(SavePlaybackDto dto, int threshold) {
        User currentUser = SecurityContextHelper.getCurrentUser();

        Playback createdPlayback;
        if (Objects.nonNull(dto.getMbTrackId()) && Objects.nonNull(dto.getMbReleaseGroupId())) {
            createdPlayback = createPlaybackWithMbids(currentUser, dto.getMbTrackId(), dto.getMbReleaseGroupId());
        } else if (!CollectionUtils.isEmpty(dto.getArtists())
                && Objects.nonNull(dto.getTitle()) && Objects.nonNull(dto.getAlbum())) {
            createdPlayback = createPlaybackWithNames(currentUser, dto.getArtists(), dto.getTitle(), dto.getAlbum(),
                    threshold);
        } else {
            throw new ApiArgumentsInvalidException(ApiArgumentsInvalidException.EC_ARGS_INSUFFICIENT,
                    "Insufficient data to create a playback.");
        }
        createdPlayback.setOriginalDataFromDto(dto);
        createdPlayback.setIdentifier(UUID.randomUUID());
        return playbackRepository.save(createdPlayback);
    }

    private Playback createPlaybackWithMbids(User currentUser, UUID trackId, UUID releaseGroupId) {
        Recording recording = recordingBoundaryService.findOrCreateRecordingWithMbid(trackId);
        ReleaseGroup releaseGroup = releaseGroupBoundaryService.findOrCreateReleaseGroupWithMusicbrainz(releaseGroupId);
        Playback playback = new Playback(recording, releaseGroup, currentUser, Date.from(Instant.now()));
        return playback;
    }

    private Playback createPlaybackWithNames(User currentUser, List<String> artists, String title, String album,
                                             int threshold) {
        MbPlaybackDetailsDto playbackDetailsDto;
        try {
            playbackDetailsDto = mbsRestClient.identifyPlaybackWithNames(artists, title, album, threshold);
        } catch (Exception ex) {
            return new Playback(null, null, currentUser, Date.from(Instant.now()));
        }

        Recording recording = recordingBoundaryService.findOrCreateRecordingWithMbid(
                playbackDetailsDto.getRecordingReference().getIdentifier());

        ReleaseGroup releaseGroup = releaseGroupBoundaryService.findOrCreateReleaseGroupWithMusicbrainz(
                playbackDetailsDto.getReleaseGroupReference().getIdentifier());

        Playback playback = new Playback(recording, releaseGroup, currentUser, Date.from(Instant.now()));
        return playback;
    }

    @Override
    public Page<Playback> findAll(Pageable pageable) {
        return playbackRepository.findAll(pageable);
    }

    @Override
    public Page<Playback> findAllForCurrentUser(boolean onlyBroken, Pageable pageable) {
        User currentUser = SecurityContextHelper.getCurrentUser();
        if (onlyBroken) {
            return playbackRepository.findByUserAndRecording(currentUser, null, pageable);
        } else {
            return playbackRepository.findByUser(currentUser, pageable);
        }
    }

    @Override
    public void deletePlayback(UUID identifier, User authenticatedUser) {
        Playback playback = playbackRepository.findOneByIdentifier(identifier);
        if (playback == null) {
            throw new EntityNotFoundException(EntityNotFoundException.EC_UNKNOWN_IDENTIFIER,
                    "No playback found for identifier {}.", identifier);
        }

        // TODO: do this permission check elsewhere
        if (!authenticatedUser.equals(playback.getUser())) {
            throw new UnauthorizedException(UnauthorizedException.EC_PLAYBACK_DELETE,
                    "Not allowed to delete playback with ID {}. You can only delete your playbacks.",
                    identifier);
        }

        playbackRepository.delete(playback);
    }

    @Override
    public Playback updatePlayback(UUID identifier, SavePlaybackDto body, User authenticatedUser) {
        Playback playback = playbackRepository.findOneByIdentifier(identifier);

        if (playback == null) {
            throw new EntityNotFoundException(EntityNotFoundException.EC_UNKNOWN_IDENTIFIER,
                    "No playback found for identifier {}", identifier);
        }

        // TODO: do this permission check elsewhere
        if (!authenticatedUser.equals(playback.getUser())) {
            throw new UnauthorizedException(UnauthorizedException.EC_PLAYBACK_FIX,
                    "Not allowed to fix playback with ID {}. You can only fix your playbacks.",
                    identifier);
        }

        if (body.getMbTrackId() != null) {
            Recording foundRecording = recordingBoundaryService.findOrCreateRecordingWithMbid(body.getMbTrackId());
            playback.setRecording(foundRecording);
        }
        if (body.getMbReleaseGroupId() != null) {
            ReleaseGroup foundReleaseGroup = releaseGroupBoundaryService.findOrCreateReleaseGroupWithMusicbrainz(
                    body.getMbReleaseGroupId());
            playback.setReleaseGroup(foundReleaseGroup);
        }
        if (!CollectionUtils.isEmpty(body.getArtists())) {
            playback.setOriginalArtists(body.getArtists());
        }
        if (body.getTitle() != null) {
            playback.setOriginalTitle(body.getTitle());
        }
        if (body.getAlbum() != null) {
            playback.setOriginalAlbum(body.getAlbum());
        }
        if (body.getLength() != null) {
            playback.setOriginalLength(body.getLength());
        }
        if (body.getDiscNumber() != null) {
            playback.setOriginalDiscNumber(body.getDiscNumber());
        }
        if (body.getTrackNumber() != null) {
            playback.setOriginalTrackNumber(body.getTrackNumber());
        }
        if (body.getPlayTime() != null) {
            playback.setPlayTime(body.getPlayTime());
        }
        return playbackRepository.save(playback);
    }

    @Override
    public Playback detectAndUpdateMbDetails(UUID identifier, int threshold, User authenticatedUser) {
        Playback playback = playbackRepository.findOneByIdentifier(identifier);

        if (playback == null) {
            throw new EntityNotFoundException(EntityNotFoundException.EC_UNKNOWN_IDENTIFIER,
                    "No playback found for identifier {}", identifier);
        }

        // TODO: do this permission check elsewhere
        if (!authenticatedUser.equals(playback.getUser())) {
            throw new UnauthorizedException(UnauthorizedException.EC_PLAYBACK_FIX,
                    "Not allowed to fix playback with ID {}. You can only fix your playbacks.", identifier);
        }

        try {
            MbPlaybackDetailsDto playbackDetailsDto = mbsRestClient.identifyPlaybackWithNames(
                    playback.getOriginalArtists(), playback.getOriginalTitle(), playback.getOriginalAlbum(), threshold);
            Recording recording = recordingBoundaryService.findOrCreateRecordingWithMbid(
                    playbackDetailsDto.getRecordingReference().getIdentifier());
            ReleaseGroup releaseGroup = releaseGroupBoundaryService.findOrCreateReleaseGroupWithMusicbrainz(
                    playbackDetailsDto.getReleaseGroupReference().getIdentifier());
            playback.setRecording(recording);
            playback.setReleaseGroup(releaseGroup);
        } catch (Exception ex) {
            logger.info(String.format("Tried to auto fix playback {} but an exception occurred.", identifier), ex);
        }
        return playback;
    }
}
