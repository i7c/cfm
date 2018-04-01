package org.rliz.mbs.artist.model

import org.rliz.mbs.common.model.AbstractEntity
import java.io.Serializable
import java.util.Date
import javax.persistence.Column
import javax.persistence.Entity
import javax.persistence.FetchType
import javax.persistence.Index
import javax.persistence.OneToMany
import javax.persistence.Table

@Entity
@Table(
    name = "artist_credit",
    indexes = [Index(name = "artist_credit_pkey", columnList = "id")]
)
class ArtistCredit : AbstractEntity(), Serializable {

    @Column(name = "name")
    val name: String? = null

    @Column(name = "artist_count")
    val artistCount: Int? = null

    @Column(name = "ref_count")
    val refCount: Int? = null

    @Column(name = "created")
    val created: Date? = null

    @OneToMany(mappedBy = "artistCredit", fetch = FetchType.LAZY)
    val artistCreditName: Set<ArtistCreditName>? = null
}
