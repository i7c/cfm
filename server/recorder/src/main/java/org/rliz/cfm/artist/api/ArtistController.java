package org.rliz.cfm.artist.api;

import org.rliz.cfm.artist.api.dto.ArtistDto;
import org.rliz.cfm.artist.api.dto.factory.ArtistListDtoFactory;
import org.rliz.cfm.artist.boundary.ArtistBoundaryService;
import org.rliz.cfm.common.api.dto.ListDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * REST controller for {@link org.rliz.cfm.artist.model.Artist}s.
 */
@RestController
@RequestMapping("/api/artists")
public class ArtistController {

    private ArtistBoundaryService artistBoundaryService;
    private ArtistListDtoFactory artistListDtoFactory;

    @Autowired
    public ArtistController(ArtistBoundaryService artistBoundaryService, ArtistListDtoFactory artistListDtoFactory) {
        this.artistBoundaryService = artistBoundaryService;
        this.artistListDtoFactory = artistListDtoFactory;
    }

    /**
     * Returns a list of artists in a paged manner.
     *
     * @param pageable the pageable
     * @return list of artist resources
     */
    @RequestMapping
    public ListDto<ArtistDto> findAll(Pageable pageable) {
        return artistListDtoFactory.build(artistBoundaryService.findAllArtists(pageable));
    }

}
