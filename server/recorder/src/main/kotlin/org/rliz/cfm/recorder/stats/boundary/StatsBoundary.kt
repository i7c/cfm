package org.rliz.cfm.recorder.stats.boundary

import org.rliz.cfm.recorder.mbs.service.MbsService
import org.rliz.cfm.recorder.stats.data.FirstClassStatsType
import org.rliz.cfm.recorder.stats.data.StatsRepo
import org.rliz.cfm.recorder.user.data.User
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.data.domain.PageImpl
import org.springframework.data.domain.Pageable
import org.springframework.stereotype.Service
import java.util.UUID

@Service
class StatsBoundary {

    @Autowired
    lateinit var statsRepo: StatsRepo

    @Autowired
    lateinit var mbsService: MbsService

    fun getFirstClassStats(type: FirstClassStatsType, user: User?, pageable: Pageable) =
        statsRepo.getFirstClassStats(
            type = type,
            userOid = user?.oid,
            pageable = pageable
        ).let { page ->
            when (type) {
                FirstClassStatsType.RECORDINGS -> translateRecordingStats(page)
                FirstClassStatsType.RELEASE_GROUPS -> translateReleaseGroupStats(page)
            }
        }

    private fun translateRecordingStats(page: PageImpl<Pair<Long, UUID>>) = mbsService
        .getRecordingView(page.content.map(Pair<Long, UUID>::second))
        .get()
        .toIdMap().let { idMap ->
            page.map { entry ->
                val mbsRef = idMap[entry.second]

                FirstClassStatsEntry(
                    count = entry.first,
                    id = entry.second,
                    artists = mbsRef?.artists ?: emptyList(),
                    title = mbsRef?.name ?: ""
                )
            }
        }

    private fun translateReleaseGroupStats(page: PageImpl<Pair<Long, UUID>>) = mbsService
        .getReleaseGroupView(page.content.map(Pair<Long, UUID>::second))
        .get()
        .toIdMap().let { idMap ->

            page.map { entry ->
                val mbsRef = idMap[entry.second]

                FirstClassStatsEntry(
                    count = entry.first,
                    id = entry.second,
                    artists = mbsRef?.artists ?: emptyList(),
                    title = mbsRef?.name ?: ""
                )
            }
        }
}

data class FirstClassStatsEntry(
    val count: Long,
    val id: UUID,
    val artists: List<String>,
    val title: String
)
