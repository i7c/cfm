package org.rliz.mbs.recording.data

import org.rliz.mbs.artist.model.ArtistCredit
import org.rliz.mbs.common.model.FirstClassEntity
import org.rliz.mbs.release.model.Medium
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
    name = "track",
    indexes = [
        Index(name = "track_pkey", columnList = "id"),
        Index(name = "track_idx_gid", columnList = "gid"),
        Index(name = "track_idx_artist_credit", columnList = "artist_credit"),
        Index(name = "track_idx_medium_position", columnList = "medium, position"),
        Index(name = "track_idx_name", columnList = "name"),
        Index(name = "track_idx_recording", columnList = "recording")
    ]
)
class Track : FirstClassEntity() {

    //    edits_pending | integer

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "recording")
    val recording: Recording? = null

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "medium")
    val medium: Medium? = null

    @Column(name = "position")
    val position: Int? = null

    @Column(name = "number")
    val number: String? = null

    @Column(name = "name")
    val name: String? = null

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "artist_credit")
    val artistCredit: ArtistCredit? = null

    @Column(name = "length")
    val length: Int? = null

    @Column(name = "last_updated")
    val lastUpdated: Date? = null

    @Column(name = "is_data_track")
    val dataTrack: Boolean? = null
}
