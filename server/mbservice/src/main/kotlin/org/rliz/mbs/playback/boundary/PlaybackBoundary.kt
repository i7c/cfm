package org.rliz.mbs.playback.boundary

import org.rliz.mbs.common.error.NotFoundException
import org.rliz.mbs.playback.data.IdentifiedPlayback
import org.rliz.mbs.playback.data.PlaybackRepo
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class PlaybackBoundary {

    @Autowired
    private lateinit var playbackRepo: PlaybackRepo

    @Transactional(readOnly = true)
    fun find(
        artist: String,
        release: String,
        recording: String,
        length: Long,
        limit: Int = 20,
        minArtistSim: Double = 0.2,
        minReleaseGroupSim: Double = 0.2,
        minRecordingSim: Double = 0.2
    ): List<IdentifiedPlayback> =
        playbackRepo.find(artist, release, recording, length, limit)
            .filter {
                it.artistSim > minArtistSim &&
                    it.releaseGroupSim > minReleaseGroupSim &&
                    it.recordingSim > minRecordingSim
            }

    @Transactional
    fun findBestMatch(
        artist: String,
        release: String,
        recording: String,
        length: Long,
        minArtistSim: Double = 0.2,
        minReleaseGroupSim: Double = 0.2,
        minRecordingSim: Double = 0.2
    ): IdentifiedPlayback =
        find(
            artist,
            release,
            recording,
            length,
            1,
            minArtistSim,
            minReleaseGroupSim,
            minRecordingSim
        ).apply {
            if (isEmpty()) throw NotFoundException(
                "artist=$artist",
                "release=$release",
                "recording=$recording",
                "length=$length",
                "minArtistSim=$minArtistSim",
                "minReleaseGroupSim=$minReleaseGroupSim",
                "minRecordingSim=$minRecordingSim"
            )
        }[0]
}
