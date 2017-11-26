package org.rliz.cfm.recorder.playback.api

import org.rliz.cfm.recorder.artist.data.Artist
import org.rliz.cfm.recorder.playback.boundary.PlaybackDto
import org.rliz.cfm.recorder.playback.data.Playback
import java.util.*
import javax.validation.constraints.NotNull
import javax.validation.constraints.Size

data class PlaybackRes(
        @field:NotNull @field:Size(min = 1) val artists: List<String> = emptyList(),
        @field:NotNull @field:Size(min = 1) val recordingTitle: String? = null,
        @field:NotNull @field:Size(min = 1) val releaseTitle: String? = null,
        val timestamp: Long? = null,
        val playTime: Long? = null,
        val trackLength: Long? = null,
        val discNumber: Int? = null,
        val trackNumber: Int? = null,
        val broken: Boolean? = null,
        val id: UUID? = null
)

fun Playback.toDto(): PlaybackDto =
        PlaybackDto(
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

                trackLength = if (this.recording != null)
                    this.recording!!.length
                else
                    this.originalData!!.length,

                discNumber = this.originalData!!.discNumber,
                trackNumber = this.originalData!!.trackNumber,
                broken = (this.recording == null || this.releaseGroup == null),
                id = this.uuid
        )
