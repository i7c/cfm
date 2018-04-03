package org.rliz.cfm.recorder.playback.boundary

import com.fasterxml.jackson.databind.ObjectMapper
import org.rliz.cfm.recorder.common.data.contentMap
import org.rliz.cfm.recorder.common.exception.MbsLookupFailedException
import org.rliz.cfm.recorder.common.exception.NotFoundException
import org.rliz.cfm.recorder.common.exception.OutdatedException
import org.rliz.cfm.recorder.common.log.logger
import org.rliz.cfm.recorder.common.security.currentUser
import org.rliz.cfm.recorder.fingerprint.boundary.FingerprintBoundary
import org.rliz.cfm.recorder.mbs.service.MbsService
import org.rliz.cfm.recorder.playback.api.PlaybackRes
import org.rliz.cfm.recorder.playback.data.NowPlaying
import org.rliz.cfm.recorder.playback.data.NowPlayingRepo
import org.rliz.cfm.recorder.playback.data.Playback
import org.rliz.cfm.recorder.playback.data.PlaybackRepo
import org.rliz.cfm.recorder.playback.data.RawPlaybackData
import org.rliz.cfm.recorder.playback.data.RawPlaybackDataRepo
import org.rliz.cfm.recorder.user.boundary.UserBoundary
import org.rliz.cfm.recorder.user.data.User
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import org.springframework.util.IdGenerator
import java.time.Instant
import java.util.UUID
import java.util.concurrent.ExecutionException

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
    lateinit var mbsService: MbsService

    @Autowired
    lateinit var nowPlayingRepo: NowPlayingRepo

    @Autowired
    lateinit var objectMapper: ObjectMapper

    @Autowired
    lateinit var fingerprintBoundary: FingerprintBoundary

    fun createPlayback(
        artists: List<String>,
        recordingTitle: String,
        releaseTitle: String,
        trackLength: Long? = null,
        playTime: Long? = null,
        discNumber: Int? = null,
        trackNumber: Int? = null,
        playbackTimestamp: Long? = null,
        source: String?
    ): PlaybackDto {

        val rawPlaybackData = rawPlaybackDataRepo.save(
            RawPlaybackData(
                artists = artists,
                artistJson = objectMapper.writeValueAsString(artists),
                recordingTitle = recordingTitle,
                releaseTitle = releaseTitle,
                length = trackLength,
                discNumber = discNumber,
                trackNumber = trackNumber
            )
        )

        val user = userBoundary.getCurrentUser()
        val timestamp = playbackTimestamp ?: Instant.now().epochSecond
        val time = playTime ?: trackLength

        val playback = Playback(idgen.generateId(), user, timestamp, time, rawPlaybackData, source)
        try {
            mbsService.identifyPlayback(recordingTitle, releaseTitle, artists)
                .get()
                .apply {
                    playback.recordingUuid = recordingId
                    playback.releaseGroupUuid = releaseGroupId
                }
        } catch (e: ExecutionException) {
            log.info("Failed to lookup details via mbs service for new playback")
            log.debug("Causing exception for failed lookup during create playback", e)
        }

        return makePlaybackView(playbackRepo.save(playback))
    }

    fun getPlaybacksForUser(userId: UUID, broken: Boolean, pageable: Pageable): Page<PlaybackDto> =
        makePlaybackView(
            if (broken)
                playbackRepo.findBrokenPlaybacksForUser(userId, pageable)
            else playbackRepo.findPlaybacksForUser(userId, pageable)
        )

    fun getAccumulatedBrokenPlaybacks(
        userId: UUID,
        pageable: Pageable
    ): Page<AccumulatedPlaybacksDto> =
        playbackRepo.findAccumulatedBrokenPlaybacks(userId, pageable)
            .map { acc ->
                acc.toDto(
                    objectMapper.readValue(
                        acc.artistsJson,
                        List::class.java
                    ) as List<String>
                )
            }

    fun fixAccumulatedPlaybacks(
        occ: Long,
        artistsJson: String,
        recordingTitle: String,
        releaseTitle: String,
        rgId: UUID?,
        recId: UUID?
    ) {
        val changedPlaybacks = playbackRepo.bulkSetRecAndRgIds(
            artistsJson,
            recordingTitle,
            releaseTitle,
            rgId,
            recId,
            Instant.now().epochSecond,
            userBoundary.getCurrentUser()
        )
        if (changedPlaybacks > occ) throw OutdatedException(Playback::class)
        // fingerprint only if this was a successful fix
        if (recId != null && rgId != null)
            fingerprintBoundary.putFingerprint(
                artistsJson,
                recordingTitle,
                releaseTitle,
                recId,
                rgId
            )
    }

    fun updatePlayback(
        playbackId: UUID,
        skipMbs: Boolean,
        artists: List<String>? = null,
        recordingTitle: String? = null,
        releaseTitle: String? = null,
        trackLength: Long? = null,
        playTime: Long? = null,
        discNumber: Int? = null,
        trackNumber: Int? = null,
        playbackTimestamp: Long? = null
    ): PlaybackDto {

        val playback = playbackRepo.findOneByUserAndUuid(currentUser(), playbackId)
            ?: throw NotFoundException(Playback::class)

        val originalData = playback.originalData!!
        if (artists != null) {
            originalData.artists = artists
            originalData.artistJson = objectMapper.writeValueAsString(artists)
        }
        if (recordingTitle != null) originalData.recordingTitle = recordingTitle
        if (releaseTitle != null) originalData.releaseTitle = releaseTitle
        if (trackLength != null) originalData.length = trackLength
        if (playTime != null) playback.playTime = playTime
        if (discNumber != null) originalData.discNumber = discNumber
        if (trackNumber != null) originalData.trackNumber = trackNumber
        if (playbackTimestamp != null) playback.timestamp = playbackTimestamp

        // Detect mbs details again, if not skipped
        if (!skipMbs) {
            try {
                mbsService.identifyPlayback(
                    originalData.recordingTitle!!,
                    originalData.releaseTitle!!,
                    originalData.artists!!
                )
                    .get()
                    .apply {
                        playback.recordingUuid = recordingId
                        playback.releaseGroupUuid = releaseGroupId
                    }
            } catch (e: ExecutionException) {
                log.info("Failed to lookup details via mbs service during PATCH; Fallback to broken playback")
                log.debug("Causing issue for failed lookup was", e)
                playback.recordingUuid = null
                playback.releaseGroupUuid = null
            }
        }
        playback.originalData = rawPlaybackDataRepo.save(originalData)
        return makePlaybackView(playbackRepo.save(playback))
    }

    fun getPlayback(playbackId: UUID): PlaybackDto =
        findPlayback(currentUser(), playbackId) ?: throw NotFoundException(Playback::class)

    fun findPlayback(user: User, playbackId: UUID): PlaybackDto? =
        playbackRepo.findOneByUserAndUuid(
            user,
            playbackId
        )?.let { makePlaybackView(listOf(it)).first() }

    @Transactional
    fun batchCreatePlaybacks(batch: List<PlaybackRes>): List<BatchResultItem> = batch.map { playbackRes ->

        /* We try to prevent here anything that could fail the entire transaction. The batch import is handled in one
        single transaction, which means one failed validation cancels the batch import. Taking out batch items that will
        should prevent this. */
        if (playbackRes.artists.isNotEmpty() &&
            playbackRes.artists.map(String::isNotBlank).isNotEmpty() &&
            playbackRes.recordingTitle != null &&
            playbackRes.recordingTitle.isNotBlank() &&
            playbackRes.releaseTitle != null &&
            playbackRes.releaseTitle.isNotBlank()) {

            val rawPlaybackData = rawPlaybackDataRepo.save(
                RawPlaybackData(
                    artists = playbackRes.artists,
                    artistJson = objectMapper.writeValueAsString(playbackRes.artists),
                    recordingTitle = playbackRes.recordingTitle,
                    releaseTitle = playbackRes.releaseTitle,
                    length = playbackRes.trackLength,
                    discNumber = playbackRes.discNumber,
                    trackNumber = playbackRes.trackNumber
                )
            )
            val user = userBoundary.getCurrentUser()
            val timestamp = playbackRes.timestamp ?: Instant.now().epochSecond
            val time = playbackRes.playTime ?: playbackRes.trackLength

            val playback = Playback(
                playbackRes.id ?: idgen.generateId(),
                user,
                timestamp,
                time,
                rawPlaybackData,
                playbackRes.source
            )
            playbackRepo.save(playback)
            true
        } else false
    }.map { BatchResultItem(it) }

    @Transactional
    fun setNowPlaying(
        artists: List<String>,
        title: String,
        release: String,
        timestamp: Long?,
        trackLength: Long?,
        idMethod: String?
    ) =
        userBoundary.getCurrentUser().let { user ->

            val nowPlaying = (nowPlayingRepo.findOneByUserUuid(user.uuid!!) ?: NowPlaying()).apply {
                this.artists = artists
                this.recordingTitle = title
                this.releaseTitle = release
                this.timestamp = (timestamp ?: Instant.now().epochSecond) + (trackLength ?: 600)
                this.user = user
                this.recordingUuid = null
                this.releaseGroupUuid = null
            }

            try {
                if (idMethod == "trigram") {
                    mbsService.identifyPlayback(artists[0], release, title, trackLength ?: 0)
                        .get().apply {
                            nowPlaying.recordingUuid = recordingId
                            nowPlaying.releaseGroupUuid = releaseGroupId
                        }
                } else {
                    mbsService.identifyPlayback(title, release, artists)
                        .get().apply {
                            nowPlaying.recordingUuid = this.recordingId
                            nowPlaying.releaseGroupUuid = this.releaseGroupId
                        }
                }
            } catch (e: ExecutionException) {
                log.info(
                    "Failed to lookup details via mbs service for new playback ({},{},{})",
                    title,
                    release,
                    artists
                )
            }
            makePlaybackView(nowPlayingRepo.save(nowPlaying))
        }

    @Transactional(readOnly = true)
    fun getNowPlaying(): PlaybackDto = (nowPlayingRepo.findOneByUserUuid(userBoundary.getCurrentUser().uuid!!)?.apply {
        if (this.timestamp!! < Instant.now().epochSecond) throw NotFoundException(
            NowPlaying::class,
            "user"
        )
    }?.let(this::makePlaybackView)) ?: throw NotFoundException(NowPlaying::class, "user")

    @Transactional
    fun deletePlaybacks(withSource: String?): Long = withSource?.let { source ->
        playbackRepo.deleteByUserAndSource(currentUser(), source)
    } ?: 0L

    private fun makePlaybackView(nowPlaying: NowPlaying): PlaybackDto = nowPlaying.let { it ->
        if (it.recordingUuid != null && it.releaseGroupUuid != null) {
            val recordingViewFuture = mbsService.getRecordingView(listOf(it.recordingUuid!!))
            val releaseGroupViewFuture = mbsService.getReleaseGroupView(listOf(it.releaseGroupUuid!!))

            try {
                val recordingView = recordingViewFuture.get().elements.first()
                val releaseGroupView = releaseGroupViewFuture.get().elements.first()

                return@let PlaybackDto(
                    artists = recordingView.artists,
                    recordingTitle = recordingView.name,
                    releaseTitle = releaseGroupView.name,
                    timestamp = it.timestamp,
                    broken = false
                )
            } catch (e: ExecutionException) {
                // nothing
            }
        }
        PlaybackDto(
            artists = it.artists!!,
            recordingTitle = it.recordingTitle!!,
            releaseTitle = it.releaseTitle!!,
            timestamp = it.timestamp,
            broken = true
        )
    }

    private fun makePlaybackView(playback: Playback): PlaybackDto = playback.let {
        makePlaybackView(
            listOf(it)
        )
    }.first()

    private fun makePlaybackView(playbacks: Page<Playback>): Page<PlaybackDto> =
        playbacks.contentMap { it -> makePlaybackView(it) }

    private fun makePlaybackView(playbacks: List<Playback>): List<PlaybackDto> =
        if (playbacks.isEmpty()) emptyList()
        else playbacks.let {
            val releaseGroupsFuture = mbsService.getReleaseGroupView(it.mapNotNull { it.releaseGroupUuid })
            val recordingsFuture = mbsService.getRecordingView(it.mapNotNull { it.recordingUuid })

            val (recordings, releaseGroups) = try {
                Pair(
                    recordingsFuture.get().elements.map { it.id to it }.toMap(),
                    releaseGroupsFuture.get().elements.map { it.id to it }.toMap()
                )
            } catch (e: Exception) {
                return@let it.map { it.toDto() }
            }

            it.map {
                if (it.recordingUuid != null) {
                    val recordingView = recordings[it.recordingUuid!!] ?: throw MbsLookupFailedException()
                    val releaseGroupView = releaseGroups[it.releaseGroupUuid!!] ?: throw MbsLookupFailedException()
                    PlaybackDto(
                        artists = recordingView.artists,
                        recordingTitle = recordingView.name,
                        releaseTitle = releaseGroupView.name,
                        timestamp = it.timestamp,
                        playTime = it.playTime,
                        broken = false,
                        id = it.uuid
                    )
                } else it.toDto()
            }
        }
}
