package org.rliz.cfm.recorder.mbs.api

import org.rliz.cfm.recorder.artist.data.Artist
import org.rliz.cfm.recorder.common.trans.nonEmptyCollection
import org.rliz.cfm.recorder.common.trans.nonNullField
import org.rliz.cfm.recorder.recording.data.Recording
import java.util.*

data class MbsRecordingRes(
        val identifier: UUID? = null,
        val lastUpdated: Long? = null,
        val length: Long? = null,
        val name: String? = null,
        val comment: String? = null,
        val artists: List<MbsArtistRes>? = null
)

data class MbsRecordingDto(
        val identifier: UUID,
        val lastUpdated: Long?,
        val length: Long?,
        val name: String,
        val comment: String? = null,
        val artists: List<MbsArtistDto>
)

fun MbsRecordingRes.toDto(): MbsRecordingDto = MbsRecordingDto(
        identifier = nonNullField(this::identifier),
        lastUpdated = this.lastUpdated,
        length = this.length,
        name = nonNullField(this::name),
        comment = this.comment,
        artists = nonEmptyCollection(this::artists).map(MbsArtistRes::toDto)
)

fun MbsRecordingDto.toEntity(artists: List<Artist> = this.artists.map(MbsArtistDto::toEntity)): Recording =
        Recording(
                uuid = this.identifier,
                title = this.name,
                lastUpdated = this.lastUpdated,
                artists = artists,
                length = this.length
        )
