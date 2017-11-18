package org.rliz.cfm.recorder.relgroup.data

import org.rliz.cfm.recorder.artist.data.Artist
import org.rliz.cfm.recorder.common.data.AbstractModel
import java.util.*
import javax.persistence.*
import javax.validation.constraints.NotNull

@Entity
class ReleaseGroup : AbstractModel {

    @NotNull
    @Column(length = 511, nullable = false)
    var title: String? = null

    var lastUpdated: Long? = null

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(foreignKey = ForeignKey(name = "FK_ReleaseGroup_Artists"),
            inverseForeignKey = ForeignKey(name = "FK_Artists_ReleaseGroup"))
    var artists: Set<Artist>? = null

    constructor() : super()

    constructor(uuid: UUID, title: String, lastUpdated: Long?, artists: List<Artist>) : super(uuid) {
        this.title = title
        this.lastUpdated = lastUpdated
        this.artists = artists.toSet()
    }

}