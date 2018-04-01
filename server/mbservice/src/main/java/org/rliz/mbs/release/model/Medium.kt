package org.rliz.mbs.release.model

import org.rliz.mbs.common.model.AbstractEntity
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
    name = "medium",
    indexes = [
        Index(name = "medium_pkey", columnList = "id"),
        Index(name = "medium_idx_release_position", columnList = "release, position"),
        Index(name = "medium_idx_track_count", columnList = "track_count")
    ]
)
class Medium : AbstractEntity() {

    //    format        | integer
    //    edits_pending | integer

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "release")
    val release: Release? = null

    @Column(name = "position")
    val position: Int? = null

    @Column(name = "name")
    val name: String? = null

    @Column(name = "last_updated")
    val lastUpdated: Date? = null

    @Column(name = "track_count")
    val trackCount: Int? = null
}
