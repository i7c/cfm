package org.rliz.mbs.release.model

import org.rliz.mbs.artist.model.ArtistCredit
import java.util.Date
import java.util.UUID

class ReleaseGroup {

    val id: Long? = null

    val gid: UUID? = null

    //    type          | integer
    //    edits_pending | integer

    val name: String? = null

    val artistCredit: ArtistCredit? = null

    val comment: String? = null

    val lastUpdated: Date? = null
}
