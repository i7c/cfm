package org.rliz.cfm.recorder.playback.data

import org.hibernate.annotations.Fetch
import org.hibernate.annotations.FetchMode
import org.rliz.cfm.recorder.user.data.User
import java.util.*
import javax.persistence.*
import javax.validation.constraints.NotNull
import javax.validation.constraints.Size

@Entity
@Table(
        indexes = arrayOf(

                Index(
                        name = "IX_NowPlaying_User",
                        columnList = "user_oid",
                        unique = true
                )
        )
)
class NowPlaying {

    @Id
    @GeneratedValue
    var oid: Long? = null

    @NotNull
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(foreignKey = ForeignKey(name = "FK_NowPlaying_User"))
    var user: User? = null

    @NotNull
    var timestamp: Long? = null

    @NotNull
    @ElementCollection(fetch = FetchType.LAZY)
    @Size(min = 1)
    @Fetch(FetchMode.SUBSELECT)
    var artists: List<String>? = null

    @NotNull
    @Size(min = 1)
    var recordingTitle: String? = null

    @NotNull
    @Size(min = 1)
    var releaseTitle: String? = null

    var recordingUuid: UUID? = null

    var releaseGroupUuid: UUID? = null

    constructor()

    constructor(artists: List<String>,
                recordingTitle: String,
                releaseTitle: String,
                timestamp: Long,
                user: User,
                recordingUuid: UUID? = null,
                releaseGroupUuid: UUID? = null) {
        this.user = user
        this.timestamp = timestamp
        this.artists = artists
        this.recordingTitle = recordingTitle
        this.releaseTitle = releaseTitle
        this.recordingUuid = recordingUuid
        this.releaseGroupUuid = releaseGroupUuid
    }
}
