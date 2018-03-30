package org.rliz.cfm.recorder.fingerprint.data

import org.rliz.cfm.recorder.common.data.AbstractModel
import org.rliz.cfm.recorder.user.data.User
import java.util.UUID
import javax.persistence.Entity
import javax.persistence.ForeignKey
import javax.persistence.Index
import javax.persistence.JoinColumn
import javax.persistence.ManyToOne
import javax.persistence.Table
import javax.validation.constraints.NotNull
import javax.validation.constraints.Size

@Entity
@Table(
    indexes = [
        Index(
            name = "IX_Fingerprint_fingerprint",
            columnList = "fingerprint"
        ),
        Index(
            name = "IX_Fingerprint_user_fingerprint",
            columnList = "user_oid, fingerprint"
        )
    ]
)
class Fingerprint : AbstractModel {

    @NotNull
    @ManyToOne
    @JoinColumn(foreignKey = ForeignKey(name = "FK_Fingerprint_User"), nullable = false)
    var user: User? = null

    @Size(min = 128, max = 128)
    var fingerprint: String? = null

    var recordingUuid: UUID? = null

    var releaseGroupUuid: UUID? = null

    constructor(
        uuid: UUID,
        user: User,
        fingerprint: String,
        recordingUuid: UUID? = null,
        releaseGroupUuid: UUID? = null
    ) : super(uuid) {
        this.user = user
        this.fingerprint = fingerprint
        this.recordingUuid = recordingUuid
        this.releaseGroupUuid = releaseGroupUuid
    }
}
