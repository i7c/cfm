package org.rliz.cfm.recorder.mbs.api

import org.rliz.cfm.recorder.artist.data.Artist
import org.rliz.cfm.recorder.common.trans.nonEmptyCollection
import org.rliz.cfm.recorder.common.trans.nonNullField
import org.rliz.cfm.recorder.relgroup.data.ReleaseGroup
import java.util.*

data class MbsReleaseGroupRes(
        val identifier: UUID? = null,
        val lastUpdated: Long? = null,
        val comment: String? = null,
        val name: String? = null,
        val artists: List<MbsArtistRes>? = null
)

data class MbsReleaseGroupDto(
        val identifier: UUID,
        val lastUpdated: Long?,
        val comment: String? = null,
        val name: String,
        val artists: List<MbsArtistDto>
)

fun MbsReleaseGroupRes.toDto(): MbsReleaseGroupDto = MbsReleaseGroupDto(
        identifier = nonNullField(this::identifier),
        lastUpdated = this.lastUpdated,
        comment = this.comment,
        name = nonNullField(this::name),
        artists = nonEmptyCollection(this::artists).map(MbsArtistRes::toDto)
)


fun MbsReleaseGroupDto.toEntity(artists: List<Artist> = this.artists.map(MbsArtistDto::toEntity)): ReleaseGroup =
        ReleaseGroup(
                uuid = this.identifier,
                title = this.name,
                lastUpdated = this.lastUpdated,
                artists = artists
        )
