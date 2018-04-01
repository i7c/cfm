package org.rliz.mbs.artist.data

import java.io.Serializable
import java.util.Date

class ArtistCredit : Serializable {

    val id: Long? = null

    val name: String? = null

    val artistCount: Int? = null

    val refCount: Int? = null

    val created: Date? = null

    val artistCreditName: Set<ArtistCreditName>? = null
}
