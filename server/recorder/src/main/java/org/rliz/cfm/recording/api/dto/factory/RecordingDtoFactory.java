package org.rliz.cfm.recording.api.dto.factory;

import org.rliz.cfm.artist.api.dto.ArtistDto;
import org.rliz.cfm.artist.api.dto.factory.ArtistDtoFactory;
import org.rliz.cfm.recording.api.dto.RecordingDto;
import org.rliz.cfm.recording.model.Recording;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Creates {@link RecordingDto}s.
 */
@Controller
public class RecordingDtoFactory {

    private ArtistDtoFactory artistListDtoFactory;

    @Autowired
    public RecordingDtoFactory(ArtistDtoFactory artistListDtoFactory) {
        this.artistListDtoFactory = artistListDtoFactory;
    }

    public RecordingDto build(Recording recording) {
        List<ArtistDto> artistDtos = recording.getArtists().stream()
                .map(artistListDtoFactory::build)
                .collect(Collectors.toList());
        RecordingDto dto = new RecordingDto(recording, artistDtos);
        return dto;
    }
}
