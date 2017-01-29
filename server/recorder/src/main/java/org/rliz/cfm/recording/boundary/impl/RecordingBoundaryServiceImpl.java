package org.rliz.cfm.recording.boundary.impl;

import org.rliz.cfm.artist.boundary.ArtistBoundaryService;
import org.rliz.cfm.artist.model.Artist;
import org.rliz.cfm.common.exception.ApiArgumentsInvalidException;
import org.rliz.cfm.common.exception.ErrorCodes;
import org.rliz.cfm.common.exception.ThirdPartyApiException;
import org.rliz.cfm.musicbrainz.api.MusicbrainzRestClient;
import org.rliz.cfm.musicbrainz.api.QueryStringBuilder;
import org.rliz.cfm.musicbrainz.api.dto.MbRecordingDto;
import org.rliz.cfm.musicbrainz.api.dto.MbRecordingListDto;
import org.rliz.cfm.recording.boundary.RecordingBoundaryService;
import org.rliz.cfm.recording.model.Recording;
import org.rliz.cfm.recording.repository.RecordingRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.validation.Validator;

import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.UUID;
import java.util.stream.Collectors;

/**
 * Implementation of {@link RecordingBoundaryService}.
 */
@Service
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
    public Recording findOrCreateRecordingWithMbid(UUID mbid) {
        Recording foundRecording = recordingRepository.findOneByMbid(mbid);
        if (foundRecording != null) {
            return foundRecording;
        }
        MbRecordingDto mbRecording = musicbrainzRestClient.getRecording(mbid, "artist-credits",
                MusicbrainzRestClient.FORMAT_JSON);
        if (mbRecording.getArtistCredits() == null) {
            throw new ThirdPartyApiException("Artist credits missing in musicbrainz answer", ErrorCodes.EC_001);
        }

        List<UUID> artistMbids = mbRecording.getArtistCredits().stream()
                .map(recording -> recording.getArtist().getMbid())
                .filter(Objects::nonNull)
                .collect(Collectors.toList());
        List<Artist> artists = artistBoundaryService.getOrCreateArtistsWithMusicbrainz(artistMbids);
        Recording createdRecording = new Recording(mbRecording.getMbid(), mbRecording.getTitle(),
                new HashSet<>(artists));
        createdRecording.setIdentifier(UUID.randomUUID());
        return recordingRepository.save(createdRecording);
    }
}
