package org.rliz.mbs.recording.data

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.jdbc.core.JdbcOperations
import org.springframework.stereotype.Service
import java.util.UUID

@Service
class RecordingRepo {

    @Autowired
    private lateinit var jdbc: JdbcOperations

    fun getRecordings(ids: List<UUID>): List<RecordingWithArtists> =
        jdbc.query(
            """
                select
                    r.gid r_gid,
                    r.name r_name,
                    r.length r_len,
                    acn.name acn_name,
                    acn.join_phrase acn_jp
                from musicbrainz.recording r
                join musicbrainz.artist_credit ac on r.artist_credit = ac.id
                join musicbrainz.artist_credit_name acn on acn.artist_credit = ac.id
                where r.gid in (?${", ?".repeat(ids.size - 1)})
                order by
                    acn.position asc
            """,
            { rs, _ ->
                RecordingWithArtist(
                    UUID.fromString(rs.getString("r_gid")),
                    rs.getString("r_name"),
                    rs.getLong("r_len"),
                    rs.getString("acn_name"),
                    rs.getString("acn_jp")
                )
            },
            ids.toTypedArray()
        ).groupBy(RecordingWithArtist::id).map { accumulate(it.value) }

    fun getRecording(id: UUID): RecordingWithArtists? =
        getRecordings(listOf(id))
            .let {
                if (it.isEmpty()) return null
                it[0]
            }

    private fun accumulate(flatLines: List<RecordingWithArtist>): RecordingWithArtists =
        RecordingWithArtists(
            flatLines[0].id,
            flatLines[0].name,
            flatLines[0].length,
            flatLines.map(RecordingWithArtist::artist),
            flatLines.fold("", { acc, next -> "$acc${next.artist}${next.joinPhrase}" })
        )
}
