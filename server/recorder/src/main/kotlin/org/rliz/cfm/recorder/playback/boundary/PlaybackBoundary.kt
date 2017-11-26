package org.rliz.cfm.recorder.playback.boundary

import org.rliz.cfm.recorder.common.data.contentMap
import org.rliz.cfm.recorder.common.exception.MbsLookupFailedException
import org.rliz.cfm.recorder.common.exception.NotFoundException
import org.rliz.cfm.recorder.common.log.logger
import org.rliz.cfm.recorder.mbs.service.MbsService
import org.rliz.cfm.recorder.playback.auth.demandOwnership
import org.rliz.cfm.recorder.playback.data.Playback
import org.rliz.cfm.recorder.playback.data.PlaybackRepo
import org.rliz.cfm.recorder.playback.data.RawPlaybackData
import org.rliz.cfm.recorder.playback.data.RawPlaybackDataRepo
import org.rliz.cfm.recorder.user.boundary.UserBoundary
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
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

    @Autowired
    lateinit var mbsService: MbsService

    fun createPlayback(artists: List<String>, recordingTitle: String, releaseTitle: String, trackLength: Long? = null,
                       playTime: Long? = null, discNumber: Int? = null, trackNumber: Int? = null,
                       playbackTimestamp: Long? = null): PlaybackDto {

        val rawPlaybackData = rawPlaybackDataRepo.save(RawPlaybackData(
                artists = artists,
                recordingTitle = recordingTitle,
                releaseTitle = releaseTitle,
                length = trackLength,
                discNumber = discNumber,
                trackNumber = trackNumber
        ))

        val user = userBoundary.getCurrentUser()
        val timestamp = playbackTimestamp ?: Instant.now().epochSecond
        val time = playTime ?: trackLength

        val playback = Playback(idgen.generateId(), user, timestamp, time, rawPlaybackData)
        try {
            val (recording, releaseGroup) =
                    playbackIdentifier.identify(recordingTitle, releaseTitle, artists)
            playback.recording = recording
            playback.releaseGroup = releaseGroup
        } catch (e: MbsLookupFailedException) {
            log.info("Failed to lookup details via mbs service for new playback")
            log.debug("Causing exception for failed lookup during create playback", e)
        }
        return makePlaybackView(playbackRepo.save(playback))
    }

    fun getPlaybacksForUser(userId: UUID, pageable: Pageable): Page<PlaybackDto> =
            makePlaybackView(playbackRepo.findPlaybacksForUser(userId, pageable))

    fun updatePlayback(playbackId: UUID,
                       skipMbs: Boolean,
                       artists: List<String>? = null,
                       recordingTitle: String? = null,
                       releaseTitle: String? = null,
                       trackLength: Long? = null,
                       playTime: Long? = null,
                       discNumber: Int? = null,
                       trackNumber: Int? = null,
                       playbackTimestamp: Long? = null): PlaybackDto {

        val playback = playbackRepo.findOneByUuid(playbackId) ?: throw NotFoundException(Playback::class)
        demandOwnership(playback)

        if (artists != null) playback.originalData!!.artists = artists
        if (recordingTitle != null) playback.originalData!!.recordingTitle = recordingTitle
        if (releaseTitle != null) playback.originalData!!.releaseTitle = releaseTitle
        if (trackLength != null) playback.originalData!!.length = trackLength
        if (playTime != null) playback.playTime = playTime
        if (discNumber != null) playback.originalData!!.discNumber = discNumber
        if (trackNumber != null) playback.originalData!!.trackNumber = trackNumber
        if (playbackTimestamp != null) playback.timestamp = playbackTimestamp

        // Detect mbs details again, if not skipped
        if (!skipMbs) {
            try {
                val (recording, releaseGroup) =
                        playbackIdentifier.identify(playback.originalData!!.recordingTitle!!,
                                playback.originalData!!.releaseTitle!!, playback.originalData!!.artists!!)
                playback.recording = recording
                playback.releaseGroup = releaseGroup
            } catch (e: MbsLookupFailedException) {
                log.info("Failed to lookup details via mbs service during PATCH; Fallback to broken playback")
                log.debug("Causing issue for failed lookup was", e)
                playback.recording = null
                playback.releaseGroup = null
            }
        }
        playback.originalData = rawPlaybackDataRepo.save(playback.originalData)
        return makePlaybackView(playbackRepo.save(playback))
    }

    fun getPlayback(playbackId: UUID): PlaybackDto =
            findPlayback(playbackId) ?: throw NotFoundException(Playback::class)

    fun findPlayback(playbackId: UUID): PlaybackDto? = playbackRepo.findOneByUuid(playbackId)?.let {
        makePlaybackView(listOf(it)).first()
    }

    private fun makePlaybackView(playback: Playback): PlaybackDto = playback.let { makePlaybackView(listOf(it)) }.first()

    private fun makePlaybackView(playbacks: Page<Playback>): Page<PlaybackDto> =
            playbacks.contentMap { it -> makePlaybackView(it) }

    private fun makePlaybackView(playbacks: List<Playback>): List<PlaybackDto> =
            playbacks.groupBy { it.recording == null || it.releaseGroup == null }
                    .flatMap { (broken, playbacks) ->
                        if (broken) playbacks.map(Playback::toDto)
                        else {
                            val releaseGroupsFuture =
                                    mbsService.getReleaseGroupView(playbacks.map { it.releaseGroup!!.uuid!! })
                            val recordingsFuture =
                                    mbsService.getRecordingView(playbacks.map { it.recording!!.uuid!! })
                            val recordings = recordingsFuture.get().elements.map { it.id to it }.toMap()
                            val releaseGroups = releaseGroupsFuture.get().elements.map { it.id to it }.toMap()
                            playbacks.map {
                                val recordingView = recordings[it.recording!!.uuid] ?: throw MbsLookupFailedException()
                                val releaseGroupView = releaseGroups[it.releaseGroup!!.uuid]
                                        ?: throw MbsLookupFailedException()
                                PlaybackDto(
                                        artists = recordingView.artists,
                                        recordingTitle = recordingView.name,
                                        releaseTitle = releaseGroupView.name,
                                        timestamp = it.timestamp,
                                        playTime = it.playTime,
                                        broken = false,
                                        id = it.uuid
                                )
                            }
                        }
                    }
}
