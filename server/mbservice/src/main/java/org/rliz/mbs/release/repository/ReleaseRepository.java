package org.rliz.mbs.release.repository;

import org.rliz.mbs.release.model.Release;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

/**
 * Repository for {@link Release}s.
 */
public interface ReleaseRepository extends JpaRepository<Release, Long> {

    @EntityGraph(attributePaths = {"artistCredit.artistCreditName.artist", "releaseGroup"})
    @Query("SELECT r FROM Release r join r.artistCredit ac join ac.artistCreditName acn join acn.artist a " +
            "where a.lowerName in (:artistNames)")
    List<Release> findReleaseByArtistNames(@Param("artistNames") List<String> artistNames);

}
