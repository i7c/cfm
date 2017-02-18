package org.rliz.cfm.release.boundary.impl;

import org.rliz.cfm.artist.boundary.ArtistBoundaryService;
import org.rliz.cfm.artist.model.Artist;
import org.rliz.cfm.mbs.dto.MbReference;
import org.rliz.cfm.mbs.dto.MbReleaseGroupDto;
import org.rliz.cfm.mbs.service.MbsRestClient;
import org.rliz.cfm.release.boundary.ReleaseGroupBoundaryService;
import org.rliz.cfm.release.model.ReleaseGroup;
import org.rliz.cfm.release.repository.ReleaseGroupRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

/**
 * Implementation for {@link ReleaseGroup}.
 */
@Service
public class ReleaseGroupBoundaryServiceImpl implements ReleaseGroupBoundaryService {

    private ReleaseGroupRepository releaseGroupRepository;

    private ArtistBoundaryService artistBoundaryService;

    private MbsRestClient mbsRestClient;

    /**
     * Constructor.
     *
     * @param releaseGroupRepository release group repo
     * @param artistBoundaryService  service for artists
     * @param mbsRestClient          client to query MBS
     */
    @Autowired
    public ReleaseGroupBoundaryServiceImpl(ReleaseGroupRepository releaseGroupRepository,
                                           ArtistBoundaryService artistBoundaryService, MbsRestClient mbsRestClient) {
        this.releaseGroupRepository = releaseGroupRepository;
        this.artistBoundaryService = artistBoundaryService;
        this.mbsRestClient = mbsRestClient;
    }

    @Override
    public ReleaseGroup findOrCreateReleaseGroupWithMusicbrainz(UUID mbid) {
        ReleaseGroup foundRg = releaseGroupRepository.findOneByMbid(mbid);
        if (foundRg != null) {
            return foundRg;
        }
        MbReleaseGroupDto mbReleaseGroup = mbsRestClient.getReleaseGroupByIdentifier(mbid);
        List<UUID> artistMbids = mbReleaseGroup.getArtistReferences().stream()
                .map(MbReference::getIdentifier)
                .collect(Collectors.toList());
        List<Artist> creditedArtists = artistBoundaryService.getOrCreateArtistsWithMusicbrainz(artistMbids);

        ReleaseGroup createdRg = new ReleaseGroup(mbReleaseGroup.getIdentifier(), mbReleaseGroup.getName(),
                new HashSet<>(creditedArtists));
        createdRg.setIdentifier(UUID.randomUUID());
        return releaseGroupRepository.save(createdRg);
    }
}
