package org.rliz.mbs.release.model

import org.rliz.mbs.artist.model.ArtistCredit
import org.rliz.mbs.common.model.FirstClassEntity
import java.util.Date
import javax.persistence.Column
import javax.persistence.Entity
import javax.persistence.FetchType
import javax.persistence.Index
import javax.persistence.JoinColumn
import javax.persistence.ManyToOne
import javax.persistence.Table

@Entity
@Table(
    name = "release",
    indexes = [
        Index(name = "release_pkey", columnList = "id"),
        Index(name = "release_idx_gid", columnList = "gid"),
        Index(name = "release_idx_artist_credit", columnList = "artist_credit"),
        Index(name = "release_idx_name", columnList = "name"),
        Index(name = "release_idx_release_group", columnList = "release_group")
    ]
)
class Release : FirstClassEntity() {

    //    status        | integer
    //    packaging     | integer
    //    language      | integer
    //    script        | integer
    //    barcode       | character varying(255)
    //    edits_pending | integer
    //    quality       | smallint

    @Column(name = "name")
    val name: String? = null

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "artist_credit")
    val artistCredit: ArtistCredit? = null

    @Column(name = "comment")
    val comment: String? = null

    @Column(name = "last_updated")
    val lastUpdated: Date? = null

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "release_group")
    val releaseGroup: ReleaseGroup? = null
}
