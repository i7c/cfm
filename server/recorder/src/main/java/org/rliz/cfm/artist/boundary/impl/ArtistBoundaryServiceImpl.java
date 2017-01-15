package org.rliz.cfm.artist.boundary.impl;

import org.rliz.cfm.artist.boundary.ArtistBoundaryService;
import org.rliz.cfm.artist.model.Artist;
import org.rliz.cfm.artist.repository.ArtistRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

/**
 * Implementation for {@link ArtistBoundaryService}.
 */
@Service
public class ArtistBoundaryServiceImpl implements ArtistBoundaryService {


    private ArtistRepository artistRepository;

    @Autowired
    public ArtistBoundaryServiceImpl(ArtistRepository artistRepository) {
        this.artistRepository = artistRepository;
    }

    @Override
    public Page<Artist> findAllArtists(Pageable pageable) {
        return artistRepository.findAll(pageable);
    }
}
