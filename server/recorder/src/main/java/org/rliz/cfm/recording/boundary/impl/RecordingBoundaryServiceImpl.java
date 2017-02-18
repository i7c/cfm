package org.rliz.cfm.recording.boundary.impl;

import org.rliz.cfm.artist.boundary.ArtistBoundaryService;
import org.rliz.cfm.artist.model.Artist;
import org.rliz.cfm.mbs.dto.MbRecordingDto;
import org.rliz.cfm.mbs.dto.MbReference;
import org.rliz.cfm.mbs.service.MbsRestClient;
import org.rliz.cfm.recording.boundary.RecordingBoundaryService;
import org.rliz.cfm.recording.model.Recording;
import org.rliz.cfm.recording.repository.RecordingRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

/**
 * Implementation of {@link RecordingBoundaryService}.
 */
@Service
public class RecordingBoundaryServiceImpl implements RecordingBoundaryService {

    private RecordingRepository recordingRepository;

    private ArtistBoundaryService artistBoundaryService;

    private MbsRestClient mbsRestClient;

    /**
     * Constructor.
     *
     * @param recordingRepository   recording repository
     * @param artistBoundaryService artist service
     * @param mbsRestClient         REST client to query MBS
     */
    @Autowired
    public RecordingBoundaryServiceImpl(RecordingRepository recordingRepository,
                                        ArtistBoundaryService artistBoundaryService, MbsRestClient mbsRestClient) {
        this.recordingRepository = recordingRepository;
        this.artistBoundaryService = artistBoundaryService;
        this.mbsRestClient = mbsRestClient;
    }

    @Override
    public Recording findOrCreateRecordingWithMbid(UUID mbid) {
        Recording foundRecording = recordingRepository.findOneByMbid(mbid);
        if (foundRecording != null) {
            return foundRecording;
        }
        MbRecordingDto mbRecording = mbsRestClient.getRecordingByIdentifier(mbid);

        List<UUID> artistMbids = mbRecording.getArtistReferences().stream()
                .map(MbReference::getIdentifier)
                .collect(Collectors.toList());
        List<Artist> artists = artistBoundaryService.getOrCreateArtistsWithMusicbrainz(artistMbids);
        Recording createdRecording = new Recording(mbRecording.getIdentifier(), mbRecording.getName(),
                new HashSet<>(artists));
        createdRecording.setIdentifier(UUID.randomUUID());
        return recordingRepository.save(createdRecording);
    }
}
