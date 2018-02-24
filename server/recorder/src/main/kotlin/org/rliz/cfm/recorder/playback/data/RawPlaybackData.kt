package org.rliz.cfm.recorder.playback.data

import org.hibernate.annotations.Fetch
import org.hibernate.annotations.FetchMode
import javax.persistence.*
import javax.validation.constraints.NotNull
import javax.validation.constraints.Size

@Entity
class RawPlaybackData {

    @Id
    @GeneratedValue
    var oid: Long? = null

    @NotNull
    @ElementCollection(fetch = FetchType.LAZY)
    @Size(min = 1)
    @Fetch(FetchMode.SUBSELECT)
    var artists: List<String>? = null

    @NotNull
    @Size(min = 1, max = 1024)
    @Column(length = 1024)
    var artistJson: String? = null

    @NotNull
    @Size(min = 1)
    var recordingTitle: String? = null

    @NotNull
    @Size(min = 1)
    var releaseTitle: String? = null

    var length: Long? = null

    var discNumber: Int? = null

    var trackNumber: Int? = null

    constructor()

    constructor(artists: List<String>?, artistJson: String?, recordingTitle: String?, releaseTitle: String?,
                length: Long? = null, discNumber: Int? = null, trackNumber: Int? = null) : this() {
        this.artists = artists
        this.artistJson = artistJson
        this.recordingTitle = recordingTitle
        this.releaseTitle = releaseTitle
        this.length = length
        this.discNumber = discNumber
        this.trackNumber = trackNumber
    }
}
