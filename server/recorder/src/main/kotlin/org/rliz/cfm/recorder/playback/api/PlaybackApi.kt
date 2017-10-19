package org.rliz.cfm.recorder.playback.api

import org.rliz.cfm.recorder.playback.boundary.PlaybackBoundary
import org.rliz.cfm.recorder.playback.trans.toHttpResponse
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.transaction.annotation.Transactional
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestMethod
import org.springframework.web.bind.annotation.RestController
import javax.validation.Valid

@RestController
@RequestMapping(path = arrayOf("/rec/v1/playbacks"))
class PlaybackApi {

    @Autowired
    lateinit var playbackBoundary: PlaybackBoundary

    @Transactional
    @RequestMapping(method = arrayOf(RequestMethod.POST))
    fun postPlayback(@Valid @RequestBody body: PlaybackRes): ResponseEntity<PlaybackRes> =
            playbackBoundary.createPlayback(
                    artists = body.artists,
                    recordingTitle = body.recordingTitle,
                    releaseTitle = body.releaseTitle,
                    trackLength = body.trackLength,
                    playTime = body.playTime,
                    discNumber = body.discNumber,
                    trackNumber = body.trackNumber,
                    playbackTimestamp = body.timestamp
            ).toHttpResponse(HttpStatus.CREATED)
}

