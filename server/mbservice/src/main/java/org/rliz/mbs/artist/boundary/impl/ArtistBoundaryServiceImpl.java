package org.rliz.mbs.artist.boundary.impl;

import org.rliz.mbs.artist.boundary.ArtistBoundaryService;
import org.rliz.mbs.artist.model.Artist;
import org.rliz.mbs.artist.repository.ArtistRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.UUID;

/**
 * Implementation of {@link ArtistBoundaryService}.
 */
@Service
public class ArtistBoundaryServiceImpl implements ArtistBoundaryService {

    private ArtistRepository artistRepository;

    @Autowired
    public ArtistBoundaryServiceImpl(ArtistRepository artistRepository) {
        this.artistRepository = artistRepository;
    }

    @Override
    public Artist getSingleArtistByIdentifier(UUID identifier) {
        return this.artistRepository.findOneByIdentifier(identifier);
    }
}
