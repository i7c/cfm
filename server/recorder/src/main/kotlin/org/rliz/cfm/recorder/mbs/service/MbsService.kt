package org.rliz.cfm.recorder.mbs.service

import org.rliz.cfm.recorder.common.exception.MbsLookupFailedException
import org.rliz.cfm.recorder.mbs.api.MbsIdentifiedPlaybackDto
import org.rliz.cfm.recorder.mbs.api.MbsIdentifiedPlaybackRes
import org.rliz.cfm.recorder.mbs.api.MbsRecordingViewListRes
import org.rliz.cfm.recorder.mbs.api.MbsReleaseGroupViewListRes
import org.springframework.beans.factory.annotation.Value
import org.springframework.scheduling.annotation.Async
import org.springframework.stereotype.Service
import org.springframework.web.client.RestTemplate
import org.springframework.web.util.UriComponentsBuilder
import java.util.UUID
import java.util.concurrent.CompletableFuture

@Service
class MbsService {

    @Value("\${cfm.mbs.url}")
    lateinit var mbsUrl: String

    @Async
    fun getRecordingView(ids: List<UUID>): CompletableFuture<MbsRecordingViewListRes> =
        UriComponentsBuilder.fromHttpUrl(mbsUrl)
            .pathSegment("mbs", "v1", "recordings")
            .let { uriBuilder ->
                ids.forEach { uriBuilder.queryParam("id", it) }
                CompletableFuture.completedFuture(
                    RestTemplate().getForObject(
                        uriBuilder.build().toUri(),
                        MbsRecordingViewListRes::class.java
                    )
                )
            }

    @Async
    fun getReleaseGroupView(ids: List<UUID>): CompletableFuture<MbsReleaseGroupViewListRes> =
        UriComponentsBuilder.fromHttpUrl(mbsUrl)
            .pathSegment("mbs", "v2", "release-groups")
            .let { uriBuilder ->
                ids.forEach { uriBuilder.queryParam("id", it) }
                CompletableFuture.completedFuture(
                    RestTemplate().getForObject(
                        uriBuilder.build().toUri(),
                        MbsReleaseGroupViewListRes::class.java
                    )
                )
            }

    @Async
    fun identifyPlayback(recordingTitle: String, releaseTitle: String, artists: List<String>):
        CompletableFuture<MbsIdentifiedPlaybackDto> =
        UriComponentsBuilder.fromHttpUrl(mbsUrl)
            .pathSegment("mbs", "v1", "playbacks", "identify")
            .queryParam("title", recordingTitle)
            .queryParam("release", releaseTitle)
            .apply {
                artists.forEach { queryParam("artist", it) }
            }
            .build()
            .toUri()
            .let { uri ->
                val future = CompletableFuture<MbsIdentifiedPlaybackDto>()
                try {
                    future.complete(
                        RestTemplate().getForObject(
                            uri,
                            MbsIdentifiedPlaybackRes::class.java
                        )!!.toDto()
                    )
                } catch (e: Exception) {
                    future.completeExceptionally(MbsLookupFailedException(e))
                }
                future
            }

    @Async
    fun identifyPlayback(
        artist: String,
        release: String,
        recording: String,
        length: Long
    ): CompletableFuture<IdentifiedPlayback> =
        UriComponentsBuilder.fromHttpUrl(mbsUrl)
            .pathSegment("mbs", "v2", "playbacks", "best")
            .queryParam("artist", artist)
            .queryParam("release", release)
            .queryParam("recording", recording)
            .queryParam("length", length)
            .build()
            .encode()
            .toUri()
            .let { uri ->
                val future = CompletableFuture<IdentifiedPlayback>()
                try {
                    future.complete(
                        RestTemplate().getForObject(uri, IdentifiedPlayback::class.java)
                    )
                } catch (e: Exception) {
                    future.completeExceptionally(MbsLookupFailedException(e))
                }
                future
            }
}
