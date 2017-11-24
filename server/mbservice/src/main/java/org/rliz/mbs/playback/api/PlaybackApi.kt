package org.rliz.mbs.playback.api

import org.rliz.mbs.common.api.toHttpResponse
import org.rliz.mbs.playback.boundary.PlaybackBoundary
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.HttpStatus
import org.springframework.transaction.annotation.Transactional
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestMethod
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/mbs/v1/playbacks")
class PlaybackApi {

    @Autowired
    private lateinit var playbackBoundary: PlaybackBoundary

    @Transactional(readOnly = true)
    @RequestMapping(method = arrayOf(RequestMethod.GET), value = "/identify")
    fun identifyPlayback(@RequestParam("artist") artist: List<String>,
                         @RequestParam("title") title: String,
                         @RequestParam("release") release: String,
                         @RequestParam(name = "res", required = false, defaultValue = "50") threshold: Int = 50) =
            playbackBoundary.identifyPlayback(artist, title, release, threshold, threshold)
                    .toRes()
                    .toHttpResponse(HttpStatus.OK)

}
