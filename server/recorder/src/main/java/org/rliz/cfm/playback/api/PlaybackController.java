package org.rliz.cfm.playback.api;

import org.rliz.cfm.common.api.dto.ListDto;
import org.rliz.cfm.playback.api.dto.CreatePlaybackDto;
import org.rliz.cfm.playback.api.dto.PlaybackDto;
import org.rliz.cfm.playback.api.dto.factory.PlaybackDtoFactory;
import org.rliz.cfm.playback.api.dto.factory.PlaybackListDtoFactory;
import org.rliz.cfm.playback.boundary.PlaybackBoundaryService;
import org.rliz.cfm.playback.model.Playback;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

/**
 * REST controller for playbacks.
 */
@RestController
@RequestMapping("/api/v1/playbacks")
public class PlaybackController {

    private PlaybackBoundaryService playbackBoundaryService;
    private PlaybackDtoFactory playbackDtoFactory;
    private PlaybackListDtoFactory playbackListDtoFactory;

    /**
     * Constructor.
     *
     * @param playbackBoundaryService playback service
     * @param playbackDtoFactory      factory for playback dtos
     * @param playbackListDtoFactory  factory for playback list dtos
     */
    @Autowired
    public PlaybackController(PlaybackBoundaryService playbackBoundaryService,
                              PlaybackDtoFactory playbackDtoFactory,
                              PlaybackListDtoFactory playbackListDtoFactory) {
        this.playbackBoundaryService = playbackBoundaryService;
        this.playbackDtoFactory = playbackDtoFactory;
        this.playbackListDtoFactory = playbackListDtoFactory;
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
        Playback playback = playbackBoundaryService.createPlayback(
                body.getMbTrackId(),
                body.getMbReleaseGroupId(),
                body.getArtists(),
                body.getTitle(),
                body.getAlbum());
        return playbackDtoFactory.build(playback);
    }

    /**
     * Requests a list of playbacks..
     *
     * @param pageable page params
     * @return list of playbacks
     */
    @Transactional(readOnly = true)
    @RequestMapping(method = RequestMethod.GET)
    public ListDto<PlaybackDto> getPlaybacks(Pageable pageable) {
        Page<Playback> playbackPage = playbackBoundaryService.findAll(pageable);
        return playbackListDtoFactory.build(playbackPage);
    }

    /**
     * Requests own playbacks
     *
     * @param pageable page params
     * @return a list of playbacks
     */
    @Transactional(readOnly = true)
    @RequestMapping(method = RequestMethod.GET, value = "/mine")
    public ListDto<PlaybackDto> getPlaybacksForUser(@PageableDefault(sort = "time", direction = Sort.Direction.DESC)
                                                            Pageable pageable) {
        Page<Playback> playbackPage = playbackBoundaryService.findAllForCurrentUser(pageable);
        return playbackListDtoFactory.build(playbackPage);
    }
}
