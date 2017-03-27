package org.rliz.mbs.playback.api;

import org.rliz.mbs.playback.api.dto.PlaybackDetailsDto;
import org.rliz.mbs.playback.api.dto.factory.PlaybackDetailsDtoFactory;
import org.rliz.mbs.playback.boundary.PlaybackBoundaryService;
import org.rliz.mbs.recording.model.Recording;
import org.rliz.mbs.release.model.Release;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.util.Pair;
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
     * @param artists list of names of artists who are credited for the release
     * @param title   the title of the recording
     * @param release the release from which this recording is played
     * @return musicbrainz details about the recording and release
     */
    @RequestMapping(method = RequestMethod.GET, value = "/identify")
    public PlaybackDetailsDto identifyPlayback(@RequestParam("artist") List<String> artists,
                                               @RequestParam("title") String title,
                                               @RequestParam("release") String release) {
        Pair<Release, Recording> result = playbackBoundaryService.identifyPlayback(artists, title, release);
        return playbackDetailsDtoFactory.build(result.getFirst(), result.getSecond());
    }

}
