package org.rliz.mbs.release.api

import org.rliz.mbs.artist.model.Artist
import org.rliz.mbs.artist.model.ArtistCreditName
import org.rliz.mbs.release.model.ReleaseGroup

data class ReleaseGroupRes(
        val name: String,
        val artists: List<String> = emptyList()
)

fun ReleaseGroup.toRes() = ReleaseGroupRes(
        name = this.name,
        artists = this.artistCredit
                .artistCreditName
                .map(ArtistCreditName::getArtist)
                .map(Artist::getName)
)
