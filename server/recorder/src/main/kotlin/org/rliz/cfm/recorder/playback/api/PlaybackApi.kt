package org.rliz.cfm.recorder.playback.api

import org.rliz.cfm.recorder.common.api.ListRes
import org.rliz.cfm.recorder.common.api.toRes
import org.rliz.cfm.recorder.common.security.currentUser
import org.rliz.cfm.recorder.playback.boundary.PlaybackBoundary
import org.rliz.cfm.recorder.playback.data.Playback
import org.rliz.cfm.recorder.playback.trans.toHttpResponse
import org.rliz.cfm.recorder.playback.trans.toRes
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.data.domain.Pageable
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.transaction.annotation.Transactional
import org.springframework.web.bind.annotation.*
import java.util.*
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


    @Transactional(readOnly = true)
    @RequestMapping
    fun getPlaybacks(@RequestParam("userId", required = false) userUuid: UUID?,
                     pageable: Pageable): ResponseEntity<ListRes<Playback, PlaybackRes>> =
            playbackBoundary.getPlaybacksForUser(userUuid ?: currentUser().uuid!!, pageable)
                    .toRes(Playback::toRes)
                    .toHttpResponse(HttpStatus.OK)

}

