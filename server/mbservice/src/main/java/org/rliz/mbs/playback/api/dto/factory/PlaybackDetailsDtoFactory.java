package org.rliz.mbs.playback.api.dto.factory;

import org.rliz.mbs.playback.api.dto.DeepPlaybackDetailsDto;
import org.rliz.mbs.playback.api.dto.PlaybackDetailsDto;
import org.rliz.mbs.recording.model.Recording;
import org.rliz.mbs.release.model.Release;
import org.springframework.stereotype.Component;

/**
 * Created by cmw on 15/02/17.
 */
@Component
public class PlaybackDetailsDtoFactory {

    public PlaybackDetailsDto build(Release release, Recording recording) {
        return new PlaybackDetailsDto(release, recording);
    }

    public DeepPlaybackDetailsDto buildFull(Release release, Recording recording) {
        return new DeepPlaybackDetailsDto(recording, release.getReleaseGroup());
    }

}
