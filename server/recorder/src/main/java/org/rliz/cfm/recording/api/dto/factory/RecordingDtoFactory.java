package org.rliz.cfm.recording.api.dto.factory;

import org.rliz.cfm.artist.api.dto.ArtistDto;
import org.rliz.cfm.artist.api.dto.factory.ArtistListDtoFactory;
import org.rliz.cfm.artist.model.Artist;
import org.rliz.cfm.common.api.dto.ListDto;
import org.rliz.cfm.recording.api.dto.RecordingDto;
import org.rliz.cfm.recording.model.Recording;
import org.springframework.stereotype.Controller;

import java.util.stream.Collectors;

/**
 * Creates {@link RecordingDto}s.
 */
@Controller
public class RecordingDtoFactory {

    private ArtistListDtoFactory artistListDtoFactory;

    public RecordingDtoFactory(ArtistListDtoFactory artistListDtoFactory) {
        this.artistListDtoFactory = artistListDtoFactory;
    }

    public RecordingDto build(Recording recording) {
        ListDto<ArtistDto> artistListDto = artistListDtoFactory
                .build(recording.getArtists().stream().collect(Collectors.toList()));
        RecordingDto dto = new RecordingDto(recording, artistListDto);
        return dto;
    }
}
