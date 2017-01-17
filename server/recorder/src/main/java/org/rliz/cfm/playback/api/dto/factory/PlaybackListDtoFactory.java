package org.rliz.cfm.playback.api.dto.factory;

import org.rliz.cfm.common.api.dto.ListDto;
import org.rliz.cfm.playback.api.dto.PlaybackDto;
import org.rliz.cfm.playback.model.Playback;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Creates list dtos of {@link PlaybackDto}s.
 */
@Controller
public class PlaybackListDtoFactory {

    private PlaybackDtoFactory playbackDtoFactory;

    @Autowired
    public PlaybackListDtoFactory(PlaybackDtoFactory playbackDtoFactory) {
        this.playbackDtoFactory = playbackDtoFactory;
    }

    public ListDto<PlaybackDto> build(Page<Playback> playbackPage) {
        List<PlaybackDto> playbackDtos = playbackPage.getContent().stream()
                .map(playbackDtoFactory::build)
                .collect(Collectors.toList());
        return new ListDto<>(playbackDtos);
    }
}
