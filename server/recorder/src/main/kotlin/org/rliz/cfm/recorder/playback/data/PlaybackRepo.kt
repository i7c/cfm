package org.rliz.cfm.recorder.playback.data

import com.fasterxml.jackson.databind.ObjectMapper
import org.rliz.cfm.recorder.common.data.getNullable
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.jdbc.core.JdbcOperations
import org.springframework.stereotype.Service
import java.time.Instant
import java.util.UUID

@Service
class PlaybackRepo {

    @Autowired
    private lateinit var jdbc: JdbcOperations

    @Autowired
    private lateinit var mapper: ObjectMapper

    fun getUnattachedPlaybackGroups(before: Long, amount: Int): List<PlaybackGroup> =
        jdbc.query(
            """
                select
                    count(*),
                    max(p.fix_attempt) last_attempt,
                    raw.recording_title rec,
                    raw.release_title rel,
                    raw.artist_json aj,
                    raw.length len
                from playback p
                join raw_playback_data raw on p.original_data_oid = raw.oid
                where (
                    recording_uuid is null
                    or release_group_uuid is null
                    )
                group by
                    raw.recording_title,
                    raw.release_title,
                    raw.artist_json,
                    raw.length
                having max(p.fix_attempt) < ?
                order by
                    last_attempt asc,
                    count desc
                limit ?;
            """,
            { rs, _ ->
                PlaybackGroup(
                    occurrences = rs.getLong("count"),
                    artists = mapper.readValue(
                        rs.getString("aj"),
                        Array<String>::class.java
                    ).toList(),
                    recordingTitle = rs.getString("rec"),
                    releaseTitle = rs.getString("rel"),
                    fixAttempt = rs.getLong("last_attempt"),
                    length = rs.getNullable("len")
                )
            },
            arrayOf(before, amount)
        )

    fun updateMbsOnPlaybackGroup(
        artists: List<String>,
        releaseTitle: String,
        recordingTitle: String,
        length: Long?,
        rgId: UUID?,
        recId: UUID?,
        fixAttempt: Long = Instant.now().epochSecond
    ) = if (length == null) updateMbsOnPlaybackGroup(
        artists = artists,
        releaseTitle = releaseTitle,
        recordingTitle = recordingTitle,
        rgId = rgId,
        recId = recId,
        fixAttempt = fixAttempt
    ) else
        jdbc.update(
            """
                update playback p
                set
                    release_group_uuid = ?,
                    recording_uuid = ?,
                    fix_attempt = ?
                from raw_playback_data raw
                where p.original_data_oid = raw.oid
                and raw.artist_json = ?
                and raw.release_title = ?
                and raw.recording_title = ?
                and raw.length = ?
            """,
            rgId,
            recId,
            fixAttempt,
            mapper.writeValueAsString(artists),
            releaseTitle,
            recordingTitle,
            length
        )

    private fun updateMbsOnPlaybackGroup(
        artists: List<String>,
        releaseTitle: String,
        recordingTitle: String,
        rgId: UUID?,
        recId: UUID?,
        fixAttempt: Long
    ) = jdbc.update(
        """
                update playback p
                set
                    release_group_uuid = ?,
                    recording_uuid = ?,
                    fix_attempt = ?
                from raw_playback_data raw
                where p.original_data_oid = raw.oid
                and raw.artist_json = ?
                and raw.release_title = ?
                and raw.recording_title = ?
                and raw.length is NULL
            """,
        rgId,
        recId,
        fixAttempt,
        mapper.writeValueAsString(artists),
        releaseTitle,
        recordingTitle
    )
}
