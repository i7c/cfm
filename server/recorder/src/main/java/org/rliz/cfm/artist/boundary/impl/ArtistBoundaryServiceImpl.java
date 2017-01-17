package org.rliz.cfm.artist.boundary.impl;

import org.rliz.cfm.artist.boundary.ArtistBoundaryService;
import org.rliz.cfm.artist.model.Artist;
import org.rliz.cfm.artist.repository.ArtistRepository;
import org.rliz.cfm.musicbrainz.api.MusicbrainzRestClient;
import org.rliz.cfm.musicbrainz.api.dto.MbArtistDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Set;
import java.util.TreeSet;
import java.util.UUID;
import java.util.stream.Collectors;

/**
 * Implementation for {@link ArtistBoundaryService}.
 */
@Service
public class ArtistBoundaryServiceImpl implements ArtistBoundaryService {

    private ArtistRepository artistRepository;
    private MusicbrainzRestClient musicbrainzRestClient;

    @Autowired
    public ArtistBoundaryServiceImpl(ArtistRepository artistRepository, MusicbrainzRestClient musicbrainzRestClient) {
        this.artistRepository = artistRepository;
        this.musicbrainzRestClient = musicbrainzRestClient;
    }

    @Override
    public Page<Artist> findAllArtists(Pageable pageable) {
        return artistRepository.findAll(pageable);
    }

    @Override
    public Artist findOneByIdentifier(UUID identifier) {
        return artistRepository.findOneByIdentifier(identifier);
    }

    @Override
    public List<Artist> getOrCreateArtistsWithMusicbrainz(List<UUID> mbids) {
        List<Artist> foundArtists = artistRepository.findByMbidIn(mbids);
        Set<UUID> unmatchedIdentifiers = new TreeSet<>(mbids);
        foundArtists.stream().forEach(artist -> unmatchedIdentifiers.remove(artist.getMbid()));

        List<MbArtistDto> mbArtists = unmatchedIdentifiers.stream()
                .map(mbid -> musicbrainzRestClient.getArtist(mbid, "json"))
                .collect(Collectors.toList());
        List<Artist> persistedArtists = mbArtists.stream().map(mbArtist -> {
            Artist createdArtist = new Artist(mbArtist.getMbid(), mbArtist.getName());
            createdArtist.setIdentifier(UUID.randomUUID());
            return artistRepository.save(createdArtist);
        }).collect(Collectors.toList());
        foundArtists.addAll(persistedArtists);
        return foundArtists;
    }
}
