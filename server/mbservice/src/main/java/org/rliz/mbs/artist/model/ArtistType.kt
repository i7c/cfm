package org.rliz.mbs.artist.model

import org.rliz.mbs.common.model.FirstClassEntity
import javax.persistence.Column
import javax.persistence.Entity
import javax.persistence.FetchType
import javax.persistence.Index
import javax.persistence.JoinColumn
import javax.persistence.ManyToOne
import javax.persistence.Table

@Entity
@Table(
    name = "artist_type",
    indexes = [
        Index(name = "artist_type_pkey", columnList = "id"),
        Index(name = "artist_type_idx_gid", columnList = "gid")
    ]
)
class ArtistType : FirstClassEntity() {

    @Column(name = "name")
    val name: String? = null

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "parent")
    val parent: ArtistType? = null

    @Column(name = "child_order", nullable = false)
    val childOrder: Int? = null

    @Column(name = "description")
    val description: String? = null
}
