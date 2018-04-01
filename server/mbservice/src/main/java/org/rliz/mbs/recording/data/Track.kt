package org.rliz.mbs.recording.data

import org.rliz.mbs.artist.model.ArtistCredit
import org.rliz.mbs.release.model.Medium
import java.util.Date
import java.util.UUID

class Track {

    val id: Long? = null

    val gid: UUID? = null

    //    edits_pending | integer

    val recording: Recording? = null

    val medium: Medium? = null

    val position: Int? = null

    val number: String? = null

    val name: String? = null

    val artistCredit: ArtistCredit? = null

    val length: Int? = null

    val lastUpdated: Date? = null

    val dataTrack: Boolean? = null
}
