package org.rliz.mbs.release.data

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.jdbc.core.JdbcOperations
import org.springframework.stereotype.Service
import java.util.UUID

@Service
class ReleaseGroupRepo {

    @Autowired
    lateinit var jdbc: JdbcOperations

    fun getReleaseGroup(id: UUID): ReleaseGroupWithArtists? =
        jdbc.query(
            """
                select
                    rg.gid rg_gid,
                    rg.name as rg_name,
                    acn.name as acn_name,
                    acn.join_phrase as acn_jp
                from musicbrainz.release_group rg
                join musicbrainz.artist_credit ac on (rg.artist_credit = ac.id)
                join musicbrainz.artist_credit_name acn on (acn.artist_credit = ac.id)
                where rg.gid = ?
                order by
                    acn.position asc
            """.trimIndent(),
            { rs, _ ->
                ReleaseGroupWithArtist(
                    UUID.fromString(rs.getString("rg_gid")),
                    rs.getString("rg_name"),
                    rs.getString("acn_jp"),
                    rs.getString("acn_name")
                )
            },
            arrayOf(id)
        ).let {
            if (it.isEmpty()) return null
            accumulate(it)
        }

    fun getReleaseGroups(ids: List<UUID>): List<ReleaseGroupWithArtists> =
        jdbc.query(
            """
                    select
                        rg.gid rg_gid,
                        rg.name as rg_name,
                        acn.name as acn_name,
                        acn.join_phrase as acn_jp
                    from musicbrainz.release_group rg
                    join musicbrainz.artist_credit ac on (rg.artist_credit = ac.id)
                    join musicbrainz.artist_credit_name acn on (acn.artist_credit = ac.id)
                    where rg.gid in (?${", ?".repeat(ids.size - 1)})
                    order by
                        acn.position asc
                """.trimIndent(),
            { rs, _ ->
                ReleaseGroupWithArtist(
                    UUID.fromString(rs.getString("rg_gid")),
                    rs.getString("rg_name"),
                    rs.getString("acn_jp"),
                    rs.getString("acn_name")
                )
            },
            ids.toTypedArray()
        ).groupBy(ReleaseGroupWithArtist::id).map { accumulate(it.value) }

    private fun accumulate(flatLines: List<ReleaseGroupWithArtist>): ReleaseGroupWithArtists =
        ReleaseGroupWithArtists(
            flatLines[0].id,
            flatLines[0].name,
            flatLines.map(ReleaseGroupWithArtist::artist),
            flatLines.fold("", { acc, next -> "$acc${next.artist}${next.joinPhrase}" })
        )
}
