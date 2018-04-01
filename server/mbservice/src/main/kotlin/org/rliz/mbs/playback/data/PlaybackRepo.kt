package org.rliz.mbs.playback.data

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.jdbc.core.JdbcOperations
import org.springframework.stereotype.Service
import java.sql.ResultSet
import java.util.UUID

@Service
class PlaybackRepo {

    @Autowired private lateinit var jdbc: JdbcOperations

    fun find(
        artist: String,
        release: String,
        recording: String,
        length: Long,
        limit: Int = 20
    ): List<IdentifiedPlayback> =
        jdbc.query(
            """
            select distinct
                a.name as artist,
                rg.name as release_group,
                rec.name as recording,
                rec.length as length,
                a.gid as artist_gid,
                rg.gid as release_group_gid,
                rec.gid as recording_gid,
                similarity(a.name, ?) as sim_artist,
                similarity(rg.name, ?) as sim_rg,
                similarity(rec.name, ?) as sim_recording,
                (? - rec.length) as length_diff
            from musicbrainz.recording rec
            join musicbrainz.track t on t.recording = rec.id
            join musicbrainz.medium med on t.medium = med.id
            join musicbrainz.release rel on med.release = rel.id
            join musicbrainz.release_group rg on rel.release_group = rg.id
            join musicbrainz.artist_credit ac on rg.artist_credit = ac.id
            join musicbrainz.artist_credit_name acn on acn.artist_credit = ac.id
            join musicbrainz.artist a on acn.artist = a.id
            left join musicbrainz.artist_alias aa on aa.artist = a.id
            where (a.name % ? or aa.name % ?)
            and rg.name % ?
            and rec.name % ?
            order by
                sim_artist desc,
                sim_rg desc,
                sim_recording desc,
                length_diff asc
            limit ?
            ;
            """.trimIndent(),
            { rs: ResultSet, _ ->
                IdentifiedPlayback(
                    rs.getString("artist"),
                    rs.getString("release_group"),
                    rs.getString("recording"),
                    UUID.fromString(rs.getString("artist_gid")),
                    UUID.fromString(rs.getString("release_group_gid")),
                    UUID.fromString(rs.getString("recording_gid")),
                    rs.getLong("length"),
                    rs.getDouble("sim_artist"),
                    rs.getDouble("sim_rg"),
                    rs.getDouble("sim_recording"),
                    rs.getLong("length_diff")
                )
            },
            arrayOf(artist, release, recording, length, artist, artist, release, recording, limit)
        )
}
