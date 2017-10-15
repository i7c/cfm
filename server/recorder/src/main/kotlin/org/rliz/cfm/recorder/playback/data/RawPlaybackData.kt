package org.rliz.cfm.recorder.playback.data

import javax.persistence.*
import javax.validation.constraints.NotNull
import javax.validation.constraints.Size

@Entity
open class RawPlaybackData {

    @Id
    @GeneratedValue
    var oid: Long? = null

    @NotNull
    @ElementCollection(fetch = FetchType.EAGER)
    @Size(min = 1)
    var artists: List<String>? = null

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

    constructor(artists: List<String>?, recordingTitle: String?, releaseTitle: String?, length: Long? = null,
                discNumber: Int? = null, trackNumber: Int? = null) : this() {
        this.artists = artists
        this.recordingTitle = recordingTitle
        this.releaseTitle = releaseTitle
        this.length = length
        this.discNumber = discNumber
        this.trackNumber = trackNumber
    }
}