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

    var lastUpdated: Long? = null

    @Size(min = 1)
    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(foreignKey = ForeignKey(name = "FK_Recording_Artists"),
            inverseForeignKey = ForeignKey(name = "FK_Artists_Recording"))
    var artists: Set<Artist>? = null

    @NotNull
    var length: Long? = null

    constructor() : super()

    constructor(uuid: UUID, title: String, lastUpdated: Long?, artists: List<Artist>, length: Long) : super(uuid) {
        this.title = title
        this.lastUpdated = lastUpdated
        this.artists = artists.toSet()
        this.length = length
    }
}