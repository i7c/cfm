package org.rliz.cfm.recorder.mbs.service

import org.rliz.cfm.recorder.common.rest.restCall
import org.rliz.cfm.recorder.mbs.api.MbsRecordingViewListRes
import org.rliz.cfm.recorder.mbs.api.MbsReleaseGroupViewListRes
import org.springframework.beans.factory.annotation.Value
import org.springframework.scheduling.annotation.Async
import org.springframework.stereotype.Service
import org.springframework.web.util.UriComponentsBuilder
import java.util.*
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
                        CompletableFuture.completedFuture(restCall().getForObject(uriBuilder.build().toUri(),
                                MbsRecordingViewListRes::class.java))
                    }

    @Async
    fun getReleaseGroupView(ids: List<UUID>): CompletableFuture<MbsReleaseGroupViewListRes> =
            UriComponentsBuilder.fromHttpUrl(mbsUrl)
                    .pathSegment("mbs", "v1", "releasegroups")
                    .let { uriBuilder ->
                        ids.forEach { uriBuilder.queryParam("id", it) }
                        CompletableFuture.completedFuture(restCall().getForObject(uriBuilder.build().toUri(),
                                MbsReleaseGroupViewListRes::class.java))
                    }
}
