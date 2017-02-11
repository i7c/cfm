package org.rliz.mbs.artist.api;

import org.rliz.mbs.artist.api.dto.ArtistDto;
import org.rliz.mbs.artist.api.dto.factory.ArtistDtoFactory;
import org.rliz.mbs.artist.api.dto.factory.ArtistListDtoFactory;
import org.rliz.mbs.artist.boundary.ArtistBoundaryService;
import org.rliz.mbs.artist.model.Artist;
import org.rliz.mbs.common.api.dto.ListDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

/**
 * API endpoint for artists.
 */
@RestController
@RequestMapping(value = "/api/v1/artists")
public class ArtistController {

    private ArtistBoundaryService artistBoundaryService;

    private ArtistDtoFactory artistDtoFactory;

    private ArtistListDtoFactory artistListDtoFactory;

    @Autowired
    public ArtistController(ArtistBoundaryService artistBoundaryService,
                            ArtistDtoFactory artistDtoFactory,
                            ArtistListDtoFactory artistListDtoFactory) {
        this.artistBoundaryService = artistBoundaryService;
        this.artistDtoFactory = artistDtoFactory;
        this.artistListDtoFactory = artistListDtoFactory;
    }

    @RequestMapping(value = "/{identifier}")
    public ArtistDto getArtistByIdentifier(@PathVariable(name = "identifier") UUID identifier) {
        Artist artist = this.artistBoundaryService.getSingleArtistByIdentifier(identifier);
        return artistDtoFactory.build(artist);
    }

    @RequestMapping(method = RequestMethod.GET)
    public ListDto<ArtistDto> findArtistByName(@RequestParam(name = "name") String name, Pageable pageable) {
        Page<Artist> foundArtists = artistBoundaryService.findArtistsByName(name, pageable);
        return artistListDtoFactory.build(foundArtists);
    }
}
