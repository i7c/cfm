package org.rliz.cfm.recorder.relgroup.boundary

import org.rliz.cfm.recorder.common.exception.InvalidResourceException
import org.rliz.cfm.recorder.relgroup.data.ReleaseGroup
import org.rliz.cfm.recorder.relgroup.data.ReleaseGroupRepo
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import java.util.*

@Service
class ReleaseGroupBoundary {

    @Autowired
    lateinit var releaseGroupRepo: ReleaseGroupRepo

    fun saveOrUpdate(new: ReleaseGroup): ReleaseGroup =
            findReleaseGroup(new.uuid ?: throw InvalidResourceException(ReleaseGroup::uuid))
                    .let { existing ->
                        when {
                            existing == null -> releaseGroupRepo.save(new)
                            existing.lastUpdated!!.before(new.lastUpdated) -> {
                                existing.artists = new.artists
                                existing.title = new.title
                                existing.lastUpdated = new.lastUpdated
                                releaseGroupRepo.save(existing)
                            }
                            else -> existing
                        }
                    }

    private fun findReleaseGroup(uuid: UUID): ReleaseGroup? = releaseGroupRepo.findOneByUuid(uuid)
}