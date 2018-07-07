package org.rliz.cfm.recorder.playback.data

import com.fasterxml.jackson.databind.ObjectMapper
import org.rliz.cfm.recorder.common.data.assertAffectedRows
import org.rliz.cfm.recorder.common.data.getNullable
import org.rliz.cfm.recorder.common.data.uuid
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.data.domain.PageImpl
import org.springframework.data.domain.Pageable
import org.springframework.jdbc.core.JdbcOperations
import org.springframework.stereotype.Service
import java.sql.ResultSet
import java.time.Instant
import java.util.UUID

@Service
class PlaybackRepo {

    @Autowired
    private lateinit var jdbc: JdbcOperations

    @Autowired
    private lateinit var mapper: ObjectMapper

    private val playbackViewColumns = """
                        uuid id,
                        raw_artists arts,
                        raw_release rel,
                        raw_recording rec,
                        play_time pt,
                        release_group_uuid rgId,
                        recording_uuid recId,
                        timestamp t
                        """

    fun save(
        id: UUID,
        playTime: Long? = null,
        releaseGroupUuid: UUID? = null,
        recordingUuid: UUID? = null,
        source: String? = null,
        timestamp: Long,
        userOid: Long,
        rawArtists: List<String>,
        rawRelease: String,
        rawRecording: String,
        rawLength: Long?

    ) =
        jdbc.update(
            """
                insert into playback
                (
                    uuid,
                    play_time,
                    release_group_uuid,
                    recording_uuid,
                    source,
                    timestamp,
                    user_oid,
                    raw_artists,
                    raw_release,
                    raw_recording,
                    raw_length
                )
                VALUES (?, ?, ?, ?, ?, ?, ?, (?)::jsonb, ?, ?, ?)
            """,
            id,
            playTime,
            releaseGroupUuid,
            recordingUuid,
            source,
            timestamp,
            userOid,
            mapper.writeValueAsString(rawArtists),
            rawRelease,
            rawRecording,
            rawLength
        ).assertAffectedRows(1)

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

    fun getByUser(userOid: Long, unattached: Boolean, pageable: Pageable): PageImpl<Playback> {
        val whereUnattached =
            if (unattached) "and (release_group_uuid is null or recording_uuid is null)" else ""

        val total = jdbc.query(
            """
                select count(*) c
                from playback p
                where p.user_oid = ?
                $whereUnattached
            """,
            { rs, _ -> rs.getLong("c") },
            arrayOf(userOid)
        ).first()

        val result = jdbc.query(
            """
                select $playbackViewColumns
                from playback p
                where p.user_oid = ?
                $whereUnattached
                order by timestamp desc
                limit ?
                offset ?
            """,
            playbackView(),
            arrayOf(userOid, pageable.pageSize, pageable.offset)
        )

        return PageImpl(result, pageable, total)
    }

    fun getByIdAndUser(uuid: UUID, userOid: Long): Playback? =
        jdbc.query(
            """
                select $playbackViewColumns
                from playback p
                where p.uuid = ?
                and p.user_oid = ?
                limit 1
            """,
            playbackView(),
            arrayOf(uuid, userOid)
        ).takeIf { it.size == 1 }?.first()

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

    private fun playbackView(): (ResultSet, Int) -> Playback {
        return { rs, _ ->
            val artists =
                mapper.readValue(rs.getString("arts"), Array<String>::class.java).toList()
            val release = rs.getString("rel")
            val recording = rs.getString("rec")

            Playback(
                rs.getString("id").uuid()!!,
                artists,
                release,
                recording,
                rs.getNullable("pt"),
                rs.getString("rgId").uuid(),
                rs.getString("recId").uuid(),
                rs.getLong("t"),
                artists,
                release,
                recording
            )
        }
    }
}
