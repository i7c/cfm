package org.rliz.cfm.recorder.playback.api

import org.rliz.cfm.recorder.playback.data.Playback
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity

fun Playback.toRes(status: HttpStatus) = ResponseEntity<PlaybackRes>(
        PlaybackRes(
                artists = this.originalData!!.artists!!,
                recordingTitle = this.originalData!!.recordingTitle!!,
                releaseTitle = this.originalData!!.releaseTitle!!,
                timestamp = this.timestamp,
                playTime = this.playTime,
                trackLength = this.originalData!!.length,
                discNumber = this.originalData!!.discNumber,
                trackNumber = this.originalData!!.trackNumber
        ), status)