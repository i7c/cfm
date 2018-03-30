package org.rliz.cfm.recorder.fingerprint.boundary

import org.apache.commons.codec.digest.DigestUtils
import org.rliz.cfm.recorder.fingerprint.data.Fingerprint
import org.rliz.cfm.recorder.fingerprint.data.FingerprintRepo
import org.rliz.cfm.recorder.user.boundary.UserBoundary
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import org.springframework.util.IdGenerator
import java.util.UUID

@Service
class FingerprintBoundary {

    @Autowired
    lateinit var fingerprintRepo: FingerprintRepo

    @Autowired
    lateinit var userBoundary: UserBoundary

    @Autowired
    lateinit var idgen: IdGenerator

    fun putFingerprint(
        artistsJson: String,
        recordingTitle: String,
        releaseTitle: String,
        recordingId: UUID,
        releaseGroupId: UUID
    ): Fingerprint =
        DigestUtils.sha512Hex(artistsJson + recordingTitle + releaseTitle).let {
            fingerprintRepo.findOneByUserAndFingerprint(userBoundary.getCurrentUser(), it)?.apply {
                recordingUuid = recordingId
                releaseGroupUuid = releaseGroupId
            } ?: Fingerprint(
                idgen.generateId(),
                userBoundary.getCurrentUser(),
                it,
                recordingId,
                releaseGroupId
            )
        }.let(fingerprintRepo::save)
}
