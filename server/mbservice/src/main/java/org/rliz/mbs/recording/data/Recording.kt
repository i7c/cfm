package org.rliz.mbs.recording.data

import org.rliz.mbs.artist.model.ArtistCredit
import org.rliz.mbs.common.model.FirstClassEntity
import java.util.Date
import javax.persistence.Column
import javax.persistence.Entity
import javax.persistence.FetchType
import javax.persistence.Index
import javax.persistence.JoinColumn
import javax.persistence.ManyToOne
import javax.persistence.OneToMany
import javax.persistence.Table

@Entity
@Table(
    name = "recording",
    indexes = [
        Index(name = "recording_pkey", columnList = "id"),
        Index(name = "recording_idx_gid", columnList = "gid"),
        Index(name = "recording_idx_artist_credit", columnList = "artist_credit"),
        Index(name = "recording_idx_name", columnList = "name")
    ]
)
class Recording : FirstClassEntity() {

    //    Not mapped yet:
    //    edits_pending | integer

    @Column(name = "name")
    val name: String? = null

    @Column(name = "length")
    val length: Long? = null

    @Column(name = "comment")
    val comment: String? = null

    @Column(name = "last_updated")
    val lastUpdated: Date? = null

    @Column(name = "video")
    val video: Boolean? = null

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "artist_credit", referencedColumnName = "id")
    val artistCredit: ArtistCredit? = null

    @OneToMany(mappedBy = "recording", fetch = FetchType.LAZY)
    val track: Set<Track>? = null
}
