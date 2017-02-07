package org.rliz.mbs.artist.api;

import org.rliz.mbs.artist.boundary.ArtistBoundaryService;
import org.rliz.mbs.artist.model.Artist;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

/**
 * API endpoint for artists.
 */
@RestController
@RequestMapping(value = "/v1/artists")
public class ArtistController {

    private ArtistBoundaryService artistBoundaryService;

    @Autowired
    public ArtistController(ArtistBoundaryService artistBoundaryService) {
        this.artistBoundaryService = artistBoundaryService;
    }

    @RequestMapping(value = "/{identifier}")
    public String getArtistByIdentifier(@PathVariable(name = "identifier") UUID identifier) {
        Artist artist = this.artistBoundaryService.getSingleArtistByIdentifier(identifier);
        return artist.toString();
    }
}
