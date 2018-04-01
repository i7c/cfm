package org.rliz.mbs.artist.model

import java.io.Serializable
import javax.persistence.Column
import javax.persistence.Entity
import javax.persistence.FetchType
import javax.persistence.Id
import javax.persistence.Index
import javax.persistence.JoinColumn
import javax.persistence.ManyToOne
import javax.persistence.Table

@Entity
@Table(
    name = "artist_credit_name",
    indexes = [
        Index(name = "artist_credit_name_pkey", columnList = "artist_credit, position"),
        Index(name = "artist_credit_name_idx_artist", columnList = "artist")
    ]
)
class ArtistCreditName : Serializable {

    @Id
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "artist_credit", referencedColumnName = "id")
    var artistCredit: ArtistCredit? = null

    @Id
    @Column(name = "position")
    var position: Int? = null

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "artist", referencedColumnName = "id")
    var artist: Artist? = null

    @Column(name = "name")
    var name: String? = null

    @Column(name = "join_phrase")
    var joinPhrase: String? = null
}
