package org.rliz.cfm.recorder.stats.data

import org.rliz.cfm.recorder.common.data.uuid
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.data.domain.PageImpl
import org.springframework.data.domain.Pageable
import org.springframework.jdbc.core.JdbcOperations
import org.springframework.stereotype.Service

@Service
class StatsRepo {

    @Autowired
    lateinit var jdbc: JdbcOperations

    fun getFirstClassStats(type: FirstClassStatsType, userOid: Long?, pageable: Pageable) =
        PageImpl(
            jdbc.query(
                """
                select
                    count(*) c,
                    p.${type.column()} id
                from playback p
                where
                    ${type.column()} is not null
                    ${maybeWhereUserEquals(userOid)}
                group by ${type.column()}
                order by c desc
                limit ?
                offset ?
            """,
                { rs, _ -> Pair(rs.getLong("c"), rs.getString("id").uuid()!!) },
                arrayOf(pageable.pageSize, pageable.offset)
            ),
            pageable,
            jdbc.query(
                """
                select count(*) c
                from (
                    select 1
                    from playback p
                    where
                        ${type.column()} is not null
                        ${maybeWhereUserEquals(userOid)}
                    group by ${type.column()}
                ) sq;
            """
            ) { rs, _ -> rs.getLong("c") }.first()
        )

    private fun maybeWhereUserEquals(forUserOid: Long?) =
        forUserOid?.let { "and user_oid = $it" } ?: ""
}

enum class FirstClassStatsType {

    RECORDINGS {
        override fun column(): String = "recording_uuid"
    },

    RELEASE_GROUPS {
        override fun column(): String = "release_group_uuid"
    };

    abstract fun column(): String
}
