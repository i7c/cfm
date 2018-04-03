package org.rliz.mbs.release.boundary

import org.rliz.mbs.common.error.NotFoundException
import org.rliz.mbs.release.data.ReleaseGroupRepo
import org.rliz.mbs.release.data.ReleaseGroupWithArtists
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import java.util.UUID

@Service
class ReleaseGroupBoundary {

    @Autowired
    private lateinit var releaseGroupRepo: ReleaseGroupRepo

    fun getReleaseGroup(id: UUID): ReleaseGroupWithArtists =
        releaseGroupRepo.getReleaseGroup(id) ?: throw NotFoundException("id=$id")
}
