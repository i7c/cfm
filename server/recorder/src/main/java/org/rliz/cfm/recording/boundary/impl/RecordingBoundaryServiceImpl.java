package org.rliz.cfm.recording.boundary.impl;

import org.rliz.cfm.artist.boundary.ArtistBoundaryService;
import org.rliz.cfm.artist.model.Artist;
import org.rliz.cfm.musicbrainz.api.MusicbrainzRestClient;
import org.rliz.cfm.musicbrainz.api.dto.MbArtistCreditsDto;
import org.rliz.cfm.musicbrainz.api.dto.MbRecordingDto;
import org.rliz.cfm.recording.boundary.RecordingBoundaryService;
import org.rliz.cfm.recording.model.Recording;
import org.rliz.cfm.recording.repository.RecordingRepository;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.*;
import java.util.stream.Collectors;

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
    public RecordingBoundaryServiceImpl(RecordingRepository recordingRepository,
                                        MusicbrainzRestClient musicbrainzRestClient,
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
        MbRecordingDto mbRecording = musicbrainzRestClient.getRecording(mbid, "artist-credits", "json");

        List<UUID> artistMbids = mbRecording.getArtistCredits().stream()
                .map(MbArtistCreditsDto::getMbid)
                .filter(Objects::nonNull)
                .collect(Collectors.toList());
        List<Artist> artists = artistBoundaryService.getOrCreateArtistsWithMusicbrainz(artistMbids);
        Recording createdRecording = new Recording(mbRecording.getMbid(), mbRecording.getTitle(),
                new TreeSet<>(artists));
        return recordingRepository.save(createdRecording);
    }
}
