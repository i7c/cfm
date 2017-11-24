package org.rliz.mbs.playback.api

import org.rliz.mbs.artist.model.Artist
import org.rliz.mbs.artist.model.ArtistCreditName
import org.rliz.mbs.recording.data.Recording
import org.rliz.mbs.release.model.Release
import java.util.*

data class IdentifiedPlaybackRes(val releaseId: UUID,
                                 val releaseGroupId: UUID,
                                 val recordingId: UUID,
                                 val recordingArtistIds: List<UUID>,
                                 val releaseGroupArtistIds: List<UUID>)

fun Pair<Release, Recording>.toRes() = this.let { (release, recording) ->
    IdentifiedPlaybackRes(
            releaseId = release.identifier,
            releaseGroupId = release.releaseGroup.identifier,
            recordingId = recording.identifier,
            recordingArtistIds = recording.artistCredit
                    .artistCreditName
                    .map(ArtistCreditName::getArtist)
                    .map(Artist::getIdentifier),
            releaseGroupArtistIds = release.releaseGroup
                    .artistCredit
                    .artistCreditName
                    .map(ArtistCreditName::getArtist)
                    .map(Artist::getIdentifier)
    )
}
