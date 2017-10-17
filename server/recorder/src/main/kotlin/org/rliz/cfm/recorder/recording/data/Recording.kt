package org.rliz.cfm.recorder.recording.data

import org.rliz.cfm.recorder.artist.data.Artist
import org.rliz.cfm.recorder.common.data.AbstractModel
import java.util.*
import javax.persistence.*
import javax.validation.constraints.NotNull
import javax.validation.constraints.Size

@Entity
class Recording : AbstractModel {

    @NotNull
    @Column(length = 512, nullable = false)
    var title: String? = null

    @NotNull
    @Temporal(TemporalType.TIMESTAMP)
    var lastUpdated: Date? = null

    @Size(min = 1)
    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(foreignKey = ForeignKey(name = "FK_Recording_Artists"),
            inverseForeignKey = ForeignKey(name = "FK_Artists_Recording"))
    var artists: List<Artist>? = null

    constructor() : super()

    constructor(uuid: UUID, title: String, lastUpdated: Date, artists: List<Artist>?) : super(uuid) {
        this.title = title
        this.lastUpdated = lastUpdated
        this.artists = artists
    }
}