package org.rliz.cfm.recorder.playback.trans

import org.rliz.cfm.recorder.artist.data.Artist
import org.rliz.cfm.recorder.playback.api.PlaybackRes
import org.rliz.cfm.recorder.playback.data.Playback
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity

fun Playback.toRes(status: HttpStatus) = ResponseEntity<PlaybackRes>(
        PlaybackRes(
                artists = if (this.recording != null)
                    this.recording!!.artists!!.mapNotNull(Artist::name).toList()
                else
                    this.originalData!!.artists!!,

                recordingTitle = if (this.recording != null)
                    this.recording!!.title!!
                else
                    this.originalData!!.recordingTitle!!,

                releaseTitle = if (this.releaseGroup != null)
                    this.releaseGroup!!.title!!
                else
                    this.originalData!!.releaseTitle!!,

                timestamp = this.timestamp,
                playTime = this.playTime,
                trackLength = this.originalData!!.length,
                discNumber = this.originalData!!.discNumber,
                trackNumber = this.originalData!!.trackNumber,
                broken = (this.recording == null || this.releaseGroup == null)),
        status)
