package org.rliz.cfm.recorder.playback.api

import org.rliz.cfm.recorder.common.api.toHttpResponse
import org.rliz.cfm.recorder.common.api.toRes
import org.rliz.cfm.recorder.common.security.currentUser
import org.rliz.cfm.recorder.playback.boundary.PlaybackBoundary
import org.rliz.cfm.recorder.playback.data.Playback
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.data.domain.Pageable
import org.springframework.data.domain.Sort
import org.springframework.data.web.SortDefault
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestMethod
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController
import java.util.UUID
import javax.validation.Valid

@RestController
@RequestMapping(path = ["/rec/v1/playbacks"])
class PlaybackApi {

    @Autowired
    lateinit var playbackBoundary: PlaybackBoundary

    @RequestMapping(method = [RequestMethod.POST])
    fun postPlayback(
        @Valid @RequestBody body: PlaybackRes,
        @RequestParam(name = "id-method", required = false) idMethod: String?
    ): ResponseEntity<PlaybackRes> =
        playbackBoundary.createPlayback(
            id = body.id,
            artists = body.artists,
            release = body.releaseTitle!!,
            recording = body.recordingTitle!!,
            length = body.trackLength,
            playtime = body.playTime,
            timestamp = body.timestamp,
            source = body.source,
            idMethod = idMethod
        ).toRes().toHttpResponse(HttpStatus.CREATED)

    @RequestMapping(method = [RequestMethod.GET])
    fun getPlaybacks(
        @RequestParam("userId", required = false) userId: UUID?,
        @RequestParam("broken", required = false, defaultValue = "false") unattached: Boolean,
        @SortDefault(sort = ["timestamp"], direction = Sort.Direction.DESC) pageable: Pageable
    ) =
        playbackBoundary.getPlaybacksForUser(userId ?: currentUser().uuid!!, unattached, pageable)
            .toRes(Playback::toRes).toHttpResponse(HttpStatus.OK)

    @RequestMapping(method = [RequestMethod.GET], path = ["/{playbackId}"])
    fun getPlayback(@PathVariable("playbackId") playbackId: UUID): ResponseEntity<PlaybackRes> =
        playbackBoundary.getPlayback(playbackId).toRes().toHttpResponse(HttpStatus.OK)

    @RequestMapping(method = [RequestMethod.POST], path = ["/batch"])
    fun postPlaybackBatch(@RequestBody batch: PlaybackBatchRes) =
        playbackBoundary.batchCreatePlaybacks(batch.playbacks).toRes().toHttpResponse(HttpStatus.OK)

    @RequestMapping(method = [RequestMethod.PUT], path = ["/now"])
    fun putNowPlaying(
        @RequestBody body: PlaybackRes,
        @RequestParam(name = "id-method", required = false) idMethod: String?
    ) =
        playbackBoundary.setNowPlaying(
            artists = body.artists,
            release = body.releaseTitle!!,
            recording = body.recordingTitle!!,
            timestamp = body.timestamp,
            trackLength = body.trackLength,
            idMethod = idMethod
        ).toRes().toHttpResponse(HttpStatus.OK)

    @RequestMapping(method = [RequestMethod.GET], path = ["/now"])
    fun getNowPlaying() = playbackBoundary.getNowPlaying().toRes().toHttpResponse(HttpStatus.OK)

    @RequestMapping(method = [RequestMethod.DELETE])
    fun deletePlaybacks(@RequestParam(required = true) withSource: String?) =
        playbackBoundary.deletePlaybacks(withSource).toRes().toHttpResponse(HttpStatus.OK)
}
