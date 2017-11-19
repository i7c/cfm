package org.rliz.mbs.recording.repository

import org.rliz.mbs.recording.model.Recording
import org.rliz.mbs.release.model.ReleaseGroup
import org.springframework.data.jpa.repository.EntityGraph
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.query.Param
import java.util.*

interface RecordingRepository : JpaRepository<Recording, Long> {

    @Query("""
        select distinct r
        from Recording r
        join fetch r.track t
        join fetch t.medium m
        join fetch m.release re
        join fetch r.artistCredit ac
        join fetch ac.artistCreditName acn
        join fetch acn.artist
        where re.releaseGroup = :releaseGroup
    """)
    fun findAllByReleaseGroup(@Param("releaseGroup") rg: ReleaseGroup): List<Recording>

    @EntityGraph(attributePaths = arrayOf("artistCredit.artistCreditName.artist"))
    fun findByIdentifier(identifier: UUID): Recording

    @Query("""
        select distinct r
        from Recording r
        join fetch r.track t
        join fetch t.medium m
        join fetch m.release re
        join fetch re.releaseGroup rg
        join fetch r.artistCredit ac
        join fetch ac.artistCreditName acn
        join fetch acn.artist
        where rg.identifier = :releaseGroupId
    """)
    fun findAllByReleaseGroupIdentifier(@Param("releaseGroupId") identifier: UUID): List<Recording>
}
