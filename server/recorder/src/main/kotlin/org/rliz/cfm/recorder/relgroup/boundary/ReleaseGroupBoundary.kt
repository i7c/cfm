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

    fun saveOrUpdate(rg: ReleaseGroup): ReleaseGroup =
            findReleaseGroup(rg.uuid ?: throw InvalidResourceException(ReleaseGroup::uuid))
                    .let {
                        when {
                            it == null -> releaseGroupRepo.save(rg)
                            it.lastUpdated!!.before(rg.lastUpdated) -> {
                                it.artists = rg.artists
                                it.title = rg.title
                                it.lastUpdated = rg.lastUpdated
                                releaseGroupRepo.save(it)
                            }
                            else -> it
                        }
                    }

    private fun findReleaseGroup(uuid: UUID): ReleaseGroup? = releaseGroupRepo.findOneByUuid(uuid)
}