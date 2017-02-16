package org.rliz.mbs.release.repository;

import org.rliz.mbs.release.model.ReleaseGroup;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

/**
 * Repository for {@link ReleaseGroup}s.
 */
public interface ReleaseGroupRepository extends JpaRepository<ReleaseGroup, Long> {

    /**
     * Retrieve single {@link ReleaseGroup} by its UUID.
     * @param identifier the UUID
     * @return the {@link ReleaseGroup}
     */
    @EntityGraph(attributePaths = "artistCredit.artistCreditName.artist")
    ReleaseGroup findByIdentifier(UUID identifier);

}
