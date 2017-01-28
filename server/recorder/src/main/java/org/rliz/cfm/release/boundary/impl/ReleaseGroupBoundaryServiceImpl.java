package org.rliz.cfm.release.boundary.impl;

import org.rliz.cfm.artist.boundary.ArtistBoundaryService;
import org.rliz.cfm.artist.model.Artist;
import org.rliz.cfm.common.exception.ErrorCodes;
import org.rliz.cfm.common.exception.ThirdPartyApiException;
import org.rliz.cfm.musicbrainz.api.MusicbrainzRestClient;
import org.rliz.cfm.musicbrainz.api.dto.MbReleaseGroupDto;
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
    private MusicbrainzRestClient musicbrainzRestClient;
    private ArtistBoundaryService artistBoundaryService;

    /**
     * Constructor.
     *
     * @param releaseGroupRepository release group repo
     * @param musicbrainzRestClient  REST client for musicbrainz API
     * @param artistBoundaryService  service for artists
     */
    @Autowired
    public ReleaseGroupBoundaryServiceImpl(ReleaseGroupRepository releaseGroupRepository, MusicbrainzRestClient musicbrainzRestClient, ArtistBoundaryService artistBoundaryService) {
        this.releaseGroupRepository = releaseGroupRepository;
        this.musicbrainzRestClient = musicbrainzRestClient;
        this.artistBoundaryService = artistBoundaryService;
    }

    @Override
    public ReleaseGroup findOrCreateReleaseGroupWithMusicbrainz(UUID mbid) {
        ReleaseGroup foundRg = releaseGroupRepository.findOneByMbid(mbid);
        if (foundRg != null) {
            return foundRg;
        }
        MbReleaseGroupDto mbReleaseGroup = musicbrainzRestClient.getReleaseGroup(mbid, "artist-credits",
                MusicbrainzRestClient.FORMAT_JSON);
        if (mbReleaseGroup.getArtistCredits() == null) {
            throw new ThirdPartyApiException("Release group did not contain artist credits", ErrorCodes.EC_001);
        }
        List<UUID> artistMbids = mbReleaseGroup.getArtistCredits().stream()
                .map(credit -> credit.getArtist().getMbid())
                .collect(Collectors.toList());
        List<Artist> creditedArtists = artistBoundaryService.getOrCreateArtistsWithMusicbrainz(artistMbids);

        ReleaseGroup createdRg = new ReleaseGroup(mbReleaseGroup.getMbid(), mbReleaseGroup.getTitle(),
                new HashSet<>(creditedArtists));
        createdRg.setIdentifier(UUID.randomUUID());
        return releaseGroupRepository.save(createdRg);
    }
}
