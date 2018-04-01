package org.rliz.mbs.playback.api

import org.rliz.mbs.playback.boundary.PlaybackBoundary
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestMethod
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/mbs/v2/playbacks")
class PlaybackApi {

    @Autowired
    private lateinit var playbackBoundary: PlaybackBoundary

    @RequestMapping(method = [RequestMethod.GET])
    fun getPlaybacks(
        @RequestParam("artist") artist: String,
        @RequestParam("release") release: String,
        @RequestParam("recording") recording: String,
        @RequestParam(name = "length", defaultValue = "0", required = false) length: Long,
        @RequestParam(name = "min-sim", defaultValue = "0.2", required = false) minSim: Double
    ) = playbackBoundary.find(artist, release, recording, length, 20, minSim, minSim, minSim)

    @RequestMapping(path = ["/best"], method = [RequestMethod.GET])
    fun getBestMatchingPlayback(
        @RequestParam("artist") artist: String,
        @RequestParam("release") release: String,
        @RequestParam("recording") recording: String,
        @RequestParam(name = "length", defaultValue = "0", required = false) length: Long,
        @RequestParam(name = "min-sim", defaultValue = "0.2", required = false) minSim: Double
    ) = playbackBoundary.findBestMatch(artist, release, recording, length, minSim, minSim, minSim)
}
