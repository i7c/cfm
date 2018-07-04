package org.rliz.cfm.recorder.playback.boundary

import org.rliz.cfm.recorder.common.data.contentMap
import org.rliz.cfm.recorder.common.exception.NotFoundException
import org.rliz.cfm.recorder.common.log.logger
import org.rliz.cfm.recorder.common.security.currentUser
import org.rliz.cfm.recorder.mbs.api.MbsRecordingViewRes
import org.rliz.cfm.recorder.mbs.api.MbsReleaseGroupViewRes
import org.rliz.cfm.recorder.mbs.service.MbsService
import org.rliz.cfm.recorder.playback.api.PlaybackRes
import org.rliz.cfm.recorder.playback.data.NowPlaying
import org.rliz.cfm.recorder.playback.data.NowPlayingRepo
import org.rliz.cfm.recorder.playback.data.Playback
import org.rliz.cfm.recorder.playback.data.PlaybackGroup
import org.rliz.cfm.recorder.playback.data.PlaybackRepo
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
    private lateinit var userBoundary: UserBoundary

    @Autowired
    private lateinit var playbackRepo: PlaybackRepo

    @Autowired
    private lateinit var idgen: IdGenerator

    @Autowired
    private lateinit var mbsService: MbsService

    @Autowired
    private lateinit var nowPlayingRepo: NowPlayingRepo

    @Transactional
    fun createPlayback(
        id: UUID?,
        artists: List<String>,
        release: String,
        recording: String,
        timestamp: Long?,
        idMethod: String?,
        length: Long? = null,
        playtime: Long? = null,
        source: String? = null
    ): Playback {
        val (rgId, recId) = identify(idMethod, artists, release, recording, length)

        val user = userBoundary.getCurrentUser()
        val sanitizedUuid = id ?: idgen.generateId()
        val sanitizedTimestamp = timestamp ?: Instant.now().epochSecond
        val sanitizedPlaytime = playtime ?: length
        playbackRepo.save(
            id = sanitizedUuid,
            playTime = sanitizedPlaytime,
            releaseGroupUuid = rgId,
            recordingUuid = recId,
            source = source,
            timestamp = sanitizedTimestamp,
            userOid = user.oid!!,
            rawArtists = artists,
            rawRelease = release,
            rawRecording = recording,
            rawLength = length
        )

        return sanitizeView(
            Playback(
                sanitizedUuid,
                artists,
                release,
                recording,
                sanitizedPlaytime,
                rgId,
                recId,
                sanitizedTimestamp,
                artists,
                release,
                recording
            )
        )
    }

    @Transactional(readOnly = true)
    fun getPlaybacksForUser(userId: UUID, broken: Boolean, pageable: Pageable): Page<Playback> =
        sanitizeView(playbackRepo.getByUser(userBoundary.getUser(userId).oid!!, broken, pageable))

    @Transactional(readOnly = true)
    fun getPlayback(playbackId: UUID): Playback =
        sanitizeView(
            findPlayback(currentUser(), playbackId) ?: throw NotFoundException(Playback::class)
        )

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

            val user = userBoundary.getCurrentUser()
            val sanitizedTimestamp = playbackRes.timestamp ?: Instant.now().epochSecond
            val sanitizedPlaytime = playbackRes.playTime ?: playbackRes.trackLength

            playbackRepo.save(
                id = playbackRes.id ?: idgen.generateId(),
                playTime = sanitizedPlaytime,
                releaseGroupUuid = null,
                recordingUuid = null,
                source = playbackRes.source,
                timestamp = sanitizedTimestamp,
                userOid = user.oid!!,
                rawArtists = playbackRes.artists,
                rawRelease = playbackRes.releaseTitle,
                rawRecording = playbackRes.recordingTitle,
                rawLength = playbackRes.trackLength
            )
            BatchResultItem(true)
        } else BatchResultItem(false)
    }

    @Transactional
    fun setNowPlaying(
        artists: List<String>,
        release: String,
        recording: String,
        timestamp: Long?,
        trackLength: Long?,
        idMethod: String?
    ) =
        userBoundary.getCurrentUser().let { user ->
            val (rgId, recId) = identify(idMethod, artists, release, recording, trackLength)
            val nowPlaying = (nowPlayingRepo.findOneByUserUuid(user.uuid!!) ?: NowPlaying()).apply {
                this.artists = artists
                this.recordingTitle = recording
                this.releaseTitle = release
                this.timestamp = (timestamp ?: Instant.now().epochSecond) + (trackLength ?: 600)
                this.user = user
                this.recordingUuid = recId
                this.releaseGroupUuid = rgId
            }
            sanitizeView(nowPlayingRepo.save(nowPlaying))
        }

    @Transactional(readOnly = true)
    fun getNowPlaying(): Playback =
        (nowPlayingRepo.findOneByUserUuid(userBoundary.getCurrentUser().uuid!!)
            ?: throw NotFoundException(NowPlaying::class, "id")
            ).let { sanitizeView(it) }

    @Transactional
    fun deletePlaybacks(withSource: String?): Long {
        throw NotImplementedError()
    }

    @Transactional(readOnly = true)
    fun getUnattachedPlaybackGroups(
        atMost: Int,
        before: Long = Instant.now().epochSecond
    ): List<PlaybackGroup> =
        playbackRepo.getUnattachedPlaybackGroups(before, atMost)

    /**
     * Call this method to update recording/release group IDs on a group of playbacks. The first
     * parameters are used to *identify* the playback group and will never be updated. The last
     * two parameters are written to all playbacks in the identified group.
     *
     * This will also set the fix_attempt field to the current time!
     *
     * Note, that it is allowed to pass null for the last two parameters. This can be used to
     * unattach a playback group as well as register a failed fix attempt for it.
     *
     * The method is user-agnostic and may update playbacks for many users at the same time.
     */
    @Transactional
    fun updateMbsOnPlaybackGroup(
        artists: List<String>,
        releaseTitle: String,
        recordingTitle: String,
        length: Long?,
        rgId: UUID?,
        recId: UUID?
    ): Int = playbackRepo.updateMbsOnPlaybackGroup(
        artists,
        releaseTitle,
        recordingTitle,
        length,
        rgId,
        recId
    )

    private fun findPlayback(user: User, playbackId: UUID): Playback? =
        playbackRepo.getByIdAndUser(playbackId, user.oid!!)

    private fun identify(
        idMethod: String?,
        artists: List<String>,
        release: String,
        recording: String,
        length: Long?
    ): Pair<UUID?, UUID?> = try {
        if (idMethod == "trigram")
            mbsService.identifyPlayback(artists[0], release, recording, length ?: 0).get()
                .let { Pair(it.releaseGroupId, it.recordingId) }
        else mbsService.identifyPlayback(recording, release, artists).get()
            .let { Pair(it.releaseGroupId, it.recordingId) }
    } catch (e: ExecutionException) {
        log.info("Failed to lookup details via mbs service for new playback")
        log.debug("Causing exception for failed lookup during create playback", e)
        Pair(null, null)
    }

    private fun sanitizeView(nowPlaying: NowPlaying): Playback =
        Playback(
            id = nowPlaying.user!!.uuid!!,
            artists = nowPlaying.artists!!,
            release = nowPlaying.releaseTitle!!,
            recording = nowPlaying.recordingTitle!!,

            releaseGroupUuid = nowPlaying.releaseGroupUuid,
            recordingUuid = nowPlaying.recordingUuid,

            timestamp = nowPlaying.timestamp!!,

            rawArtists = nowPlaying.artists!!,
            rawRelease = nowPlaying.releaseTitle!!,
            rawRecording = nowPlaying.recordingTitle!!
        ).let(::sanitizeView)

    private fun sanitizeView(playback: Playback): Playback =
        listOf(playback).let(::sanitizeView).first()

    private fun sanitizeView(playbacks: Page<Playback>): Page<Playback> =
        playbacks.contentMap(::sanitizeView)

    private fun sanitizeView(playbacks: List<Playback>): List<Playback> =
        if (playbacks.isEmpty()) emptyList()
        else playbacks.let { originalPlaybacks ->

            val releaseGroupsFuture =
                mbsService.getReleaseGroupView(originalPlaybacks.mapNotNull(Playback::releaseGroupUuid))
            val recordingsFuture =
                mbsService.getRecordingView(originalPlaybacks.mapNotNull(Playback::recordingUuid))

            try {
                Pair(
                    releaseGroupsFuture.get().elements.map { it.id to it }.toMap(),
                    recordingsFuture.get().elements.map { it.id to it }.toMap()
                )
            } catch (e: Exception) {
                Pair(mapOf<UUID, MbsReleaseGroupViewRes>(), mapOf<UUID, MbsRecordingViewRes>())
            }.let { (rgs, recs) ->
                originalPlaybacks.map {
                    val releaseGroupView = rgs[it.releaseGroupUuid]
                    val recordingView = recs[it.recordingUuid]
                    it.copy(
                        artists = recordingView?.artists ?: it.artists,
                        release = releaseGroupView?.name ?: it.release,
                        recording = recordingView?.name ?: it.recording
                    )
                }
            }
        }
}
