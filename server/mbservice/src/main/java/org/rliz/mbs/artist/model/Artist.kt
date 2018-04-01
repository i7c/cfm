package org.rliz.mbs.artist.model

import org.rliz.mbs.area.model.Area
import org.rliz.mbs.common.model.FirstClassEntity
import java.util.Date
import javax.persistence.Column
import javax.persistence.Entity
import javax.persistence.FetchType
import javax.persistence.Index
import javax.persistence.JoinColumn
import javax.persistence.ManyToOne
import javax.persistence.Table
import javax.persistence.Temporal
import javax.persistence.TemporalType

@Entity
@Table(
    name = "artist",
    indexes = [
        Index(name = "artist_pkey", columnList = "id"),
        Index(name = "artist_idx_gid", columnList = "gid"),
        Index(name = "artist_idx_null_comment", columnList = "name"),
        Index(name = "artist_idx_uniq_name_comment", columnList = "name, comment"),
        Index(name = "artist_idx_area", columnList = "area"),
        Index(name = "artist_idx_begin_area", columnList = "begin_area"),
        Index(name = "artist_idx_end_area", columnList = "end_area"),
        Index(name = "artist_idx_lower_name", columnList = "name"),
        Index(name = "artist_idx_name", columnList = "name"),
        Index(name = "artist_idx_sort_name", columnList = "sort_name")
    ]
)
class Artist : FirstClassEntity() {

    @Column(name = "name")
    val name: String? = null

    @Column(name = "sort_name")
    val sortName: String? = null

    @Column(name = "begin_date_year")
    val beginDateYear: Int? = null

    @Column(name = "begin_date_month")
    val beginDateMonth: Int? = null

    @Column(name = "begin_date_day")
    val beginDateDay: Int? = null

    @Column(name = "end_date_year")
    val endDateYear: Int? = null

    @Column(name = "end_date_month")
    val endDateMonth: Int? = null

    @Column(name = "end_date_day")
    val endDateDay: Int? = null

    @Column(name = "ended")
    val ended: Boolean? = null

    @Column(name = "comment")
    val comment: String? = null

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "type")
    val artistType: ArtistType? = null

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "area")
    val area: Area? = null

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "begin_area")
    val beginArea: Area? = null

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "end_area")
    val endArea: Area? = null

    @Column(name = "last_updated")
    @Temporal(TemporalType.TIMESTAMP)
    val lastUpdated: Date? = null

    // Not mapped:
    //    gender           | integer
    //    edits_pending    | integer
}
