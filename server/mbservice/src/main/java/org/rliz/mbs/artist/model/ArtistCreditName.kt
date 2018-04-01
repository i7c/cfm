package org.rliz.mbs.artist.model

import java.io.Serializable

class ArtistCreditName : Serializable {

    var artistCredit: ArtistCredit? = null

    var position: Int? = null

    var artist: Artist? = null

    var name: String? = null

    var joinPhrase: String? = null
}
