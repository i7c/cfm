package org.rliz.mbs.recording.data

import org.rliz.mbs.artist.model.ArtistCredit
import java.util.Date
import java.util.UUID

class Recording {

    val id: Long? = null

    val gid: UUID? = null

    //    Not mapped yet:
    //    edits_pending | integer

    val name: String? = null

    val length: Long? = null

    val comment: String? = null

    val lastUpdated: Date? = null

    val video: Boolean? = null

    val artistCredit: ArtistCredit? = null

    val track: Set<Track>? = null
}
