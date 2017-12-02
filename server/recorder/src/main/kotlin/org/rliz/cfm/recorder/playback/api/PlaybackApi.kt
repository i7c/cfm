package org.rliz.cfm.recorder.playback.api

import org.rliz.cfm.recorder.common.api.toHttpResponse
import org.rliz.cfm.recorder.common.api.toRes
import org.rliz.cfm.recorder.common.security.currentUser
import org.rliz.cfm.recorder.playback.boundary.PlaybackBoundary
import org.rliz.cfm.recorder.playback.boundary.PlaybackDto
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.data.domain.Pageable
import org.springframework.data.domain.Sort
import org.springframework.data.web.SortDefault
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
                    recordingTitle = body.recordingTitle!!,
                    releaseTitle = body.releaseTitle!!,
                    trackLength = body.trackLength,
                    playTime = body.playTime,
                    discNumber = body.discNumber,
                    trackNumber = body.trackNumber,
                    playbackTimestamp = body.timestamp
            )
                    .toRes()
                    .toHttpResponse(HttpStatus.CREATED)

    @Transactional
    @RequestMapping(method = arrayOf(RequestMethod.PATCH), path = arrayOf("/{playbackId}"))
    fun patchPlayback(@PathVariable(name = "playbackId", required = true) playbackId: UUID,
                      @RequestParam(name = "skipMbs", required = false) skipMbs: Boolean?,
                      @RequestBody body: PatchPlaybackRes): ResponseEntity<PlaybackRes> =
            playbackBoundary.updatePlayback(playbackId,
                    skipMbs ?: false,
                    artists = body.artists,
                    recordingTitle = body.recordingTitle,
                    releaseTitle = body.releaseTitle,
                    trackLength = body.trackLength,
                    playTime = body.playTime,
                    discNumber = body.discNumber,
                    trackNumber = body.trackNumber,
                    playbackTimestamp = body.timestamp
            )
                    .toRes()
                    .toHttpResponse(HttpStatus.OK)

    @Transactional(readOnly = true)
    @RequestMapping(method = arrayOf(RequestMethod.GET))
    fun getPlaybacks(@RequestParam("userId", required = false) userUuid: UUID?,
                     @RequestParam("broken", required = false, defaultValue = "false") broken: Boolean,
                     @SortDefault(sort = arrayOf("timestamp"), direction = Sort.Direction.DESC) pageable: Pageable) =
            playbackBoundary.getPlaybacksForUser(userUuid ?: currentUser().uuid!!, broken, pageable)
                    .toRes(PlaybackDto::toRes)
                    .toHttpResponse(HttpStatus.OK)

    @Transactional(readOnly = true)
    @RequestMapping(method = arrayOf(RequestMethod.GET), path = arrayOf("/{playbackId}"))
    fun getPlayback(@PathVariable("playbackId") playbackId: UUID): ResponseEntity<PlaybackRes> =
            playbackBoundary.getPlayback(playbackId)
                    .toRes()
                    .toHttpResponse(HttpStatus.OK)

    @Transactional
    @RequestMapping(method = arrayOf(RequestMethod.POST), path = arrayOf("/batch"))
    fun postPlaybackBatch(@RequestBody batch: PlaybackBatchRes) =
            playbackBoundary.batchCreatePlaybacks(batch.playbacks)
                    .toRes()
                    .toHttpResponse(HttpStatus.OK)

}

