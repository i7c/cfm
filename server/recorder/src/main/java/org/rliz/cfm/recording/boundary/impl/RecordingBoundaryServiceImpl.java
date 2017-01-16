package org.rliz.cfm.recording.boundary.impl;

import org.rliz.cfm.artist.boundary.ArtistBoundaryService;
import org.rliz.cfm.musicbrainz.api.MusicbrainzRestClient;
import org.rliz.cfm.musicbrainz.api.dto.MusicbrainzRecording;
import org.rliz.cfm.recording.boundary.RecordingBoundaryService;
import org.rliz.cfm.recording.model.Recording;
import org.rliz.cfm.recording.repository.RecordingRepository;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Collections;
import java.util.UUID;

/**
 * Implementation of {@link RecordingBoundaryService}.
 */
public class RecordingBoundaryServiceImpl implements RecordingBoundaryService {

    private static final String MUSICBRAINZ_RECORDING_URL = "http://musicbrainz.org/ws/2/recording/{mbid}";
    private static final String MUSICBRAINZ_FORMAT_PARAM = "fmt";
    private static final String MUSICBRAINZ_INCLUDE_PARAM = "inc";
    public static final String MUSICBRAINZ_JSON = "json";

    private RecordingRepository recordingRepository;
    private MusicbrainzRestClient musicbrainzRestClient;
    private ArtistBoundaryService artistBoundaryService;

    /**
     * Constructor.
     *
     * @param recordingRepository   recording repository
     * @param musicbrainzRestClient REST client to query musicbrainzdb
     * @param artistBoundaryService artist service
     */
    @Autowired
    public RecordingBoundaryServiceImpl(RecordingRepository recordingRepository, MusicbrainzRestClient musicbrainzRestClient,
                                        ArtistBoundaryService artistBoundaryService) {
        this.recordingRepository = recordingRepository;
        this.musicbrainzRestClient = musicbrainzRestClient;
        this.artistBoundaryService = artistBoundaryService;
    }

    @Override
    public Recording findOrCreateRecordingWithMusicbrainz(UUID mbid) {
        Recording foundRecording = recordingRepository.findOneByMbid(mbid);
        if (foundRecording != null) {
            return foundRecording;
        }
        MusicbrainzRecording mbRecording = musicbrainzRestClient.getRecording(mbid, "artist-credits", "json");
        Recording createdRecording = new Recording(mbRecording.getMbid(), mbRecording.getTitle(), Collections.emptySet());
        return recordingRepository.save(createdRecording);
    }
}
