package org.rliz.cfm.playback.api;

import org.rliz.cfm.playback.api.dto.CreatePlaybackDto;
import org.rliz.cfm.playback.api.dto.PlaybackDto;
import org.rliz.cfm.playback.api.dto.factory.PlaybackDtoFactory;
import org.rliz.cfm.playback.boundary.PlaybackBoundaryService;
import org.rliz.cfm.playback.model.Playback;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.transaction.Transactional;

/**
 * REST controller for playbacks.
 */
@RestController
@RequestMapping("/api/v1/playbacks")
public class PlaybackController {

    private PlaybackBoundaryService playbackBoundaryService;
    private PlaybackDtoFactory playbackDtoFactory;

    /**
     * Constructor.
     *
     * @param playbackBoundaryService playback service
     * @param playbackDtoFactory      factory for playback dtos
     */
    @Autowired
    public PlaybackController(PlaybackBoundaryService playbackBoundaryService, PlaybackDtoFactory playbackDtoFactory) {
        this.playbackBoundaryService = playbackBoundaryService;
        this.playbackDtoFactory = playbackDtoFactory;
    }

    /**
     * Makes a new {@link Playback} using the given information in the body.
     *
     * @param body playback data
     * @return the new playback resource
     */
    @Transactional
    @RequestMapping(method = RequestMethod.POST)
    public PlaybackDto createPlayback(@RequestBody CreatePlaybackDto body) {
        Playback playback = playbackBoundaryService.createPlaybackWithMbids(body.getMbTrackId(),
                body.getMbReleaseGroupId());
        return playbackDtoFactory.build(playback);
    }

}
