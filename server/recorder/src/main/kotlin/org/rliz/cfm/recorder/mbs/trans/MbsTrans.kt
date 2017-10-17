package org.rliz.cfm.recorder.mbs.trans

import org.rliz.cfm.recorder.artist.data.Artist
import org.rliz.cfm.recorder.common.trans.nonEmptyCollection
import org.rliz.cfm.recorder.common.trans.nonNullField
import org.rliz.cfm.recorder.mbs.api.*
import org.rliz.cfm.recorder.recording.data.Recording
import org.rliz.cfm.recorder.relgroup.data.ReleaseGroup


fun MbsIdentifyRes.toDto(): MbsIdentifyDto =
        MbsIdentifyDto(
                nonNullField(this::recording).toDto(),
                nonNullField(this::releaseGroup).toDto()
        )

fun MbsRecordingRes.toDto(): MbsRecordingDto = MbsRecordingDto(
        identifier = nonNullField(this::identifier),
        lastUpdated = nonNullField(this::lastUpdated),
        length = nonNullField(this::length),
        name = nonNullField(this::name),
        comment = this.comment,
        artists = nonEmptyCollection(this::artists).map(MbsArtistRes::toDto)
)

fun MbsReleaseGroupRes.toDto(): MbsReleaseGroupDto = MbsReleaseGroupDto(
        identifier = nonNullField(this::identifier),
        lastUpdated = nonNullField(this::lastUpdated),
        comment = this.comment,
        name = nonNullField(this::name),
        artists = nonEmptyCollection(this::artists).map(MbsArtistRes::toDto)
)

fun MbsArtistRes.toDto(): MbsArtistDto = MbsArtistDto(
        comment = this.comment,
        name = nonNullField(this::name),
        typeName = this.typeName,
        ended = this.ended,
        identifier = nonNullField(this::identifier),
        sortName = this.sortName,
        beginDateYear = this.beginDateYear,
        lastUpdated = nonNullField(this::lastUpdated),
        areaName = this.areaName
)


fun MbsRecordingDto.toEntity(artists: List<Artist> = this.artists.map(MbsArtistDto::toEntity)): Recording =
        Recording(
                uuid = this.identifier,
                title = this.name,
                lastUpdated = this.lastUpdated,
                artists = artists
        )

fun MbsReleaseGroupDto.toEntity(artists: List<Artist> = this.artists.map(MbsArtistDto::toEntity)): ReleaseGroup =
        ReleaseGroup(
                uuid = this.identifier,
                title = this.name,
                lastUpdated = this.lastUpdated,
                artists = artists
        )

fun MbsArtistDto.toEntity(): Artist = Artist(
        uuid = this.identifier,
        name = this.name,
        lastUpdated = this.lastUpdated
)
