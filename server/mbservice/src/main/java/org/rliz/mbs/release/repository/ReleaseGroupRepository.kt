package org.rliz.mbs.release.repository

import org.rliz.mbs.release.model.ReleaseGroup
import org.springframework.data.jpa.repository.EntityGraph
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.query.Param
import java.util.*

interface ReleaseGroupRepository : JpaRepository<ReleaseGroup, Long> {

    @EntityGraph(attributePaths = arrayOf("artistCredit.artistCreditName.artist"))
    fun findByIdentifier(identifier: UUID): ReleaseGroup

    @EntityGraph(attributePaths = arrayOf("artistCredit.artistCreditName.artist"))
    fun findByIdentifierIn(identifiers: List<UUID>): List<ReleaseGroup>

    @Query("""
        SELECT r FROM
        ReleaseGroup r
        join r.artistCredit ac
        join ac.artistCreditName acn
        join acn.artist a
        where lower(a.name) in (:artistNames)
        """)
    @EntityGraph(attributePaths = arrayOf("artistCredit.artistCreditName.artist"))
    fun findReleaseGroupByArtistNames(@Param("artistNames") artistNames: List<String>): List<ReleaseGroup>

}
