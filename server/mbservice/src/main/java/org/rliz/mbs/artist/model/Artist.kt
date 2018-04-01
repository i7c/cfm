package org.rliz.mbs.artist.model

import org.rliz.mbs.area.model.Area
import java.util.Date
import java.util.UUID

class Artist {

    val id: Long? = null

    val gid: UUID? = null

    val name: String? = null

    val sortName: String? = null

    val beginDateYear: Int? = null

    val beginDateMonth: Int? = null

    val beginDateDay: Int? = null

    val endDateYear: Int? = null

    val endDateMonth: Int? = null

    val endDateDay: Int? = null

    val ended: Boolean? = null

    val comment: String? = null

    val artistType: ArtistType? = null

    val area: Area? = null

    val beginArea: Area? = null

    val endArea: Area? = null

    val lastUpdated: Date? = null

    // Not mapped:
    //    gender           | integer
    //    edits_pending    | integer
}
