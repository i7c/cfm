package org.rliz.mbs.playback.boundary

import org.rliz.mbs.playback.data.IdentifiedPlayback
import org.rliz.mbs.playback.data.PlaybackRepo
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class PlaybackBoundary {

    @Autowired private lateinit var playbackRepo: PlaybackRepo

    @Transactional(readOnly = true)
    fun identify(
        artist: String,
        release: String,
        recording: String,
        length: Long,
        limit: Int = 10
    ): List<IdentifiedPlayback> =
        playbackRepo.identify(artist, release, recording, length, limit)
}
