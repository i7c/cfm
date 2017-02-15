package org.rliz.mbs.recording.repository;

import org.rliz.mbs.recording.model.Recording;
import org.rliz.mbs.release.model.ReleaseGroup;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

/**
 * Repo for {@link Recording}s.
 */
public interface RecordingRepository extends JpaRepository<Recording, Long> {

    @Query("SELECT r FROM Recording r join r.track t join t.medium m join m.release re " +
            "where re.releaseGroup = :releaseGroup")
    @EntityGraph(attributePaths = {"artistCredit.artistCreditName.artist", "track.medium.release.releaseGroup"})
    List<Recording> findAllByReleaseGroup(@Param("releaseGroup") ReleaseGroup rg);

}
