package org.rliz.cfm.recorder.playback.scheduler

import org.rliz.cfm.recorder.mbs.service.MbsService
import org.rliz.cfm.recorder.playback.boundary.PlaybackBoundary
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.scheduling.annotation.Scheduled
import org.springframework.stereotype.Service
import java.time.Duration
import java.time.Instant
import java.util.concurrent.ExecutionException
import java.util.concurrent.TimeUnit
import java.util.concurrent.TimeoutException

@Service
class AutoFixer {

    companion object {
        private val log = LoggerFactory.getLogger(AutoFixer::class.java)
        private const val ATTEMPT_BACKOFF_HOURS: Long = 6
        private const val BATCH_SIZE = 1000
    }

    @Autowired
    private lateinit var playbackBoundary: PlaybackBoundary

    @Autowired
    private lateinit var mbs: MbsService

    @Scheduled(fixedDelay = 3 * 60 * 1000)
    fun fixBunch() {
        log.info("Starting auto-fixer run ...")
        val unattached = playbackBoundary.getUnattachedPlaybackGroups(
            BATCH_SIZE,
            Instant.now().minus(Duration.ofHours(ATTEMPT_BACKOFF_HOURS)).epochSecond
        )
        if (unattached.isEmpty()) {
            log.info("Nothing to fix, return early.")
            return
        }

        unattached.map { group ->
            val (rgId, recId) = try {
                mbs.identifyPlayback(
                    group.artists[0],
                    group.releaseTitle,
                    group.recordingTitle,
                    group.length ?: 0
                ).get(10, TimeUnit.SECONDS).let { Pair(it.releaseGroupId, it.recordingId) }
            } catch (e: ExecutionException) {
                Pair(null, null)
            } catch (e: TimeoutException) {
                log.warn("Timed out for $group with message ${e.message}")
                Pair(null, null)
            }
            playbackBoundary.updateMbsOnPlaybackGroup(
                group.artists,
                group.releaseTitle,
                group.recordingTitle,
                group.length,
                rgId,
                recId
            )
        }.sum().apply {
            log.info("Finished run, updated $this playbacks.")
        }
    }
}
