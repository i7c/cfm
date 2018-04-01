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

    @Autowired private lateinit var playbackBoundary: PlaybackBoundary

    @RequestMapping(method = [RequestMethod.GET])
    fun findArtist(
        @RequestParam("artist") artist: String,
        @RequestParam("release") release: String,
        @RequestParam("recording") recording: String,
        @RequestParam("length") length: Long
    ) = playbackBoundary.identify(artist, release, recording, length)
}
