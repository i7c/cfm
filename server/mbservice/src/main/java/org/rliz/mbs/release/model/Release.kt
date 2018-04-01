package org.rliz.mbs.release.model

import org.rliz.mbs.artist.model.ArtistCredit
import java.util.Date
import java.util.UUID

class Release {

    val id: Long? = null

    val gid: UUID? = null

    //    status        | integer
    //    packaging     | integer
    //    language      | integer
    //    script        | integer
    //    barcode       | character varying(255)
    //    edits_pending | integer
    //    quality       | smallint

    val name: String? = null

    val artistCredit: ArtistCredit? = null

    val comment: String? = null

    val lastUpdated: Date? = null

    val releaseGroup: ReleaseGroup? = null
}
