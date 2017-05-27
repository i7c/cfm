package org.rliz.mbs.release.repository;

import org.rliz.mbs.release.model.ReleaseGroup;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.UUID;

/**
 * Repository for {@link ReleaseGroup}s.
 */
public interface ReleaseGroupRepository extends JpaRepository<ReleaseGroup, Long> {

    /**
     * Retrieve single {@link ReleaseGroup} by its UUID.
     *
     * @param identifier the UUID
     * @return the {@link ReleaseGroup}
     */
    @EntityGraph(attributePaths = "artistCredit.artistCreditName.artist")
    ReleaseGroup findByIdentifier(UUID identifier);

    /**
     * Retrieve all {@link ReleaseGroup}s for a given list of artists.
     *
     * @param artistNames the names of the credited artists
     * @return the list of found {@link ReleaseGroup}s
     */
    @EntityGraph(attributePaths = {"artistCredit.artistCreditName.artist"})
    @Query("SELECT r FROM ReleaseGroup r join r.artistCredit ac join ac.artistCreditName acn join acn.artist a " +
            "where lower(a.name) in (:artistNames)")
    List<ReleaseGroup> findReleaseGroupByArtistNames(@Param("artistNames") List<String> artistNames);

}
