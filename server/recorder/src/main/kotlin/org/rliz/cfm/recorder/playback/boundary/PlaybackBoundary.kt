package org.rliz.cfm.recorder.playback.boundary

import org.rliz.cfm.recorder.common.exception.MbsLookupFailedException
import org.rliz.cfm.recorder.common.log.logger
import org.rliz.cfm.recorder.playback.data.Playback
import org.rliz.cfm.recorder.playback.data.PlaybackRepo
import org.rliz.cfm.recorder.playback.data.RawPlaybackData
import org.rliz.cfm.recorder.playback.data.RawPlaybackDataRepo
import org.rliz.cfm.recorder.user.boundary.UserBoundary
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import org.springframework.util.IdGenerator
import java.time.Instant
import java.util.*

@Service
class PlaybackBoundary {

    companion object {
        val log = logger<PlaybackBoundary>()
    }

    @Autowired
    lateinit var userBoundary: UserBoundary

    @Autowired
    lateinit var rawPlaybackDataRepo: RawPlaybackDataRepo

    @Autowired
    lateinit var playbackRepo: PlaybackRepo

    @Autowired
    lateinit var idgen: IdGenerator

    @Autowired
    lateinit var playbackIdentifier: PlaybackIdentifier

    fun createPlayback(artists: List<String>, recordingTitle: String, releaseTitle: String, trackLength: Long? = null,
                       playTime: Long? = null, discNumber: Int? = null, trackNumber: Int? = null,
                       playbackTimestamp: Date? = null): Playback {

        val rawPlaybackData = rawPlaybackDataRepo.save(RawPlaybackData(
                artists = artists,
                recordingTitle = recordingTitle,
                releaseTitle = releaseTitle,
                length = trackLength,
                discNumber = discNumber,
                trackNumber = trackNumber
        ))

        val user = userBoundary.getCurrentUser()
        val timestamp = playbackTimestamp ?: Date.from(Instant.now())
        val time = playTime ?: trackLength

        val playback = Playback(idgen.generateId(), user, timestamp, time, rawPlaybackData)
        try {
            val (recording, releaseGroup) =
                    playbackIdentifier.identify(recordingTitle, releaseTitle, artists)
            playback.recording = recording
            playback.releaseGroup = releaseGroup
        } catch (e: MbsLookupFailedException) {
            log.error("Failed to contact MBS. Recording information could not be retrieved.")
            log.error(e.message)
        }
        return playbackRepo.save(playback)
    }
}