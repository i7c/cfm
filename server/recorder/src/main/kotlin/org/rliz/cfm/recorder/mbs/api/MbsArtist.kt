package org.rliz.cfm.recorder.mbs.api

import org.rliz.cfm.recorder.artist.data.Artist
import org.rliz.cfm.recorder.common.trans.nonNullField
import java.util.*

data class MbsArtistRes(
        val comment: String? = null,
        val name: String? = null,
        val typeName: String? = null,
        val ended: Boolean? = null,
        val identifier: UUID? = null,
        val sortName: String? = null,
        val beginDateYear: Int? = null,
        val lastUpdated: Long? = null,
        val areaName: String? = null
)

data class MbsArtistDto(
        val comment: String? = null,
        val name: String,
        val typeName: String? = null,
        val ended: Boolean? = null,
        val identifier: UUID,
        val sortName: String? = null,
        val beginDateYear: Int? = null,
        val lastUpdated: Long?,
        val areaName: String? = null
)

fun MbsArtistRes.toDto(): MbsArtistDto = MbsArtistDto(
        comment = this.comment,
        name = nonNullField(this::name),
        typeName = this.typeName,
        ended = this.ended,
        identifier = nonNullField(this::identifier),
        sortName = this.sortName,
        beginDateYear = this.beginDateYear,
        lastUpdated = this.lastUpdated,
        areaName = this.areaName
)

fun MbsArtistDto.toEntity(): Artist = Artist(
        uuid = this.identifier,
        name = this.name,
        lastUpdated = this.lastUpdated
)
