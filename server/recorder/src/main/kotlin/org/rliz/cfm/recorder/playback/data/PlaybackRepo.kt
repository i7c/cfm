package org.rliz.cfm.recorder.playback.data

import com.fasterxml.jackson.databind.ObjectMapper
import org.rliz.cfm.recorder.common.data.getNullable
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.jdbc.core.JdbcOperations
import org.springframework.stereotype.Service

@Service
class PlaybackRepo {

    @Autowired
    private lateinit var jdbc: JdbcOperations

    @Autowired
    private lateinit var mapper: ObjectMapper

    fun getUnattachedPlaybackGroups(amount: Int): List<PlaybackGroup> =
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
                where recording_uuid is null
                or release_group_uuid is null
                group by
                    raw.recording_title,
                    raw.release_title,
                    raw.artist_json,
                    raw.length
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
            arrayOf(amount)
        )
}
