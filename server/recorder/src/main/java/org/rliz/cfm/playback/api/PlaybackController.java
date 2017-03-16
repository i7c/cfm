package org.rliz.cfm.playback.api;

import org.rliz.cfm.common.api.dto.ListDto;
import org.rliz.cfm.playback.api.dto.SavePlaybackDto;
import org.rliz.cfm.playback.api.dto.PlaybackDto;
import org.rliz.cfm.playback.api.dto.factory.PlaybackDtoFactory;
import org.rliz.cfm.playback.api.dto.factory.PlaybackListDtoFactory;
import org.rliz.cfm.playback.boundary.PlaybackBoundaryService;
import org.rliz.cfm.playback.model.Playback;
import org.rliz.cfm.user.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

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
    public PlaybackDto createPlayback(@RequestBody SavePlaybackDto body) {
        Playback playback = playbackBoundaryService.createPlayback(body);
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
     * @param onlyBroken if true only broken playbacks will be included
     * @param pageable   page params
     * @return a list of playbacks
     */
    @Transactional(readOnly = true)
    @RequestMapping(method = RequestMethod.GET, value = "/mine")
    public ListDto<PlaybackDto> getPlaybacksForUser(
            @RequestParam(name = "onlyBroken", required = false) boolean onlyBroken,
            @PageableDefault(sort = "time", direction = Sort.Direction.DESC) Pageable pageable) {

        Page<Playback> playbackPage = playbackBoundaryService.findAllForCurrentUser(onlyBroken, pageable);
        return playbackListDtoFactory.build(playbackPage);
    }

    /**
     * DELETE operation on a single {@link Playback} resource.
     *
     * @param identifier the identifier of the playback
     * @param user       the authenticated user
     * @return 204 upon success
     */
    @Transactional
    @RequestMapping(method = RequestMethod.DELETE, value = "/{identifier}")
    public ResponseEntity<?> deletePlayback(@PathVariable(name = "identifier") UUID identifier,
                                            @AuthenticationPrincipal(errorOnInvalidType = true) User user) {
        playbackBoundaryService.deletePlayback(identifier, user);
        return ResponseEntity.noContent().build();
    }

    /**
     * PATCH operation on a {@link Playback} resource. The provided fields will be changed in the resource with given
     * identifier.
     *
     * @param identifier identifier of the existing {@link Playback} resource
     * @param body       body containing the update information. Only non-null fields will be applied.
     * @param user       the currently authenticated user
     * @return 200 and the updated resource
     */
    @Transactional
    @RequestMapping(method = RequestMethod.PATCH, value = "/{identifier}")
    public ResponseEntity<PlaybackDto> updatePlayback(@PathVariable(name = "identifier") UUID identifier,
                                                      @RequestBody SavePlaybackDto body,
                                                      @AuthenticationPrincipal(errorOnInvalidType = true) User user) {

        Playback fixedPlayback = playbackBoundaryService.updatePlayback(identifier, body, user);
        return new ResponseEntity<>(playbackDtoFactory.build(fixedPlayback), HttpStatus.OK);
    }
}
