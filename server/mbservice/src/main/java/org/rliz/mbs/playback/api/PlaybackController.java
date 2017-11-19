package org.rliz.mbs.playback.api;

import org.rliz.mbs.playback.api.dto.PlaybackDetailsDto;
import org.rliz.mbs.playback.api.dto.DeepPlaybackDetailsDto;
import org.rliz.mbs.playback.api.dto.factory.PlaybackDetailsDtoFactory;
import org.rliz.mbs.playback.boundary.PlaybackBoundaryService;
import org.rliz.mbs.recording.data.Recording;
import org.rliz.mbs.release.model.Release;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.util.Pair;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * API endpoint for things related to playbacks AKA a particular song played from a particular release, possibly with
 * additional information regarding the playback.
 */
@RestController
@RequestMapping("/mbs/v1/playbacks")
public class PlaybackController {

    private PlaybackBoundaryService playbackBoundaryService;

    private PlaybackDetailsDtoFactory playbackDetailsDtoFactory;

    @Autowired
    public PlaybackController(PlaybackBoundaryService playbackBoundaryService, PlaybackDetailsDtoFactory
            playbackDetailsDtoFactory) {
        this.playbackBoundaryService = playbackBoundaryService;
        this.playbackDetailsDtoFactory = playbackDetailsDtoFactory;
    }

    /**
     * Tries to identify a playback by the given information.
     *
     * @param artists   list of names of artists who are credited for the release
     * @param title     the title of the recording
     * @param release   the release from which this recording is played
     * @param threshold threshold for matching release group and recording
     * @param rgThres   threshold for matching release groups, overrides threshold
     * @param recThres  threshold for matching recordings, overrides threshold
     * @return musicbrainz details about the recording and release
     */
    @RequestMapping(method = RequestMethod.GET, value = "/identify")
    public ResponseEntity<?> identifyPlayback(@RequestParam("artist") List<String> artists,
                                              @RequestParam("title") String title,
                                              @RequestParam("release") String release,
                                              @RequestParam(name = "thres", required = false, defaultValue = "50")
                                                      Integer threshold,
                                              @RequestParam(name = "rgthres", required = false) Integer rgThres,
                                              @RequestParam(name = "recthres", required = false) Integer recThres,
                                              @RequestParam(name = "full", required = false, defaultValue = "false")
                                                      Boolean deep) {
        if (rgThres == null) {
            rgThres = threshold;
        }
        if (recThres == null) {
            recThres = threshold;
        }
        Pair<Release, Recording> result = playbackBoundaryService.identifyPlayback(artists, title, release, rgThres,
                recThres);
        if (deep) {
            return new ResponseEntity<DeepPlaybackDetailsDto>(playbackDetailsDtoFactory.buildFull(result.getFirst(),
                    result.getSecond()), HttpStatus.OK);
        } else {
            return new ResponseEntity<PlaybackDetailsDto>(playbackDetailsDtoFactory.build(result.getFirst(),
                    result.getSecond()), HttpStatus.OK);
        }
    }

}
