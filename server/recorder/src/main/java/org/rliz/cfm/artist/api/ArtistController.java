package org.rliz.cfm.artist.api;

import org.rliz.cfm.artist.api.dto.ArtistDto;
import org.rliz.cfm.artist.api.dto.CreateArtistDto;
import org.rliz.cfm.artist.api.dto.factory.ArtistDtoFactory;
import org.rliz.cfm.artist.api.dto.factory.ArtistListDtoFactory;
import org.rliz.cfm.artist.boundary.ArtistBoundaryService;
import org.rliz.cfm.artist.model.Artist;
import org.rliz.cfm.common.api.dto.ListDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.util.UUID;

/**
 * REST controller for {@link org.rliz.cfm.artist.model.Artist}s.
 */
@RestController
@RequestMapping("/api/artists")
public class ArtistController {

    private final static String PATH_VARIABLE_ID = "/{artistId}";
    private final static String VARIABLE_ID = "artistId";

    private ArtistBoundaryService artistBoundaryService;
    private ArtistDtoFactory artistDtoFactory;
    private ArtistListDtoFactory artistListDtoFactory;

    @Autowired
    public ArtistController(ArtistBoundaryService artistBoundaryService, ArtistDtoFactory artistDtoFactory, ArtistListDtoFactory artistListDtoFactory) {
        this.artistBoundaryService = artistBoundaryService;
        this.artistDtoFactory = artistDtoFactory;
        this.artistListDtoFactory = artistListDtoFactory;
    }

    /**
     * GET request to return a list of artists without filters.
     *
     * @param pageable the pageable
     * @return list of artist resources
     */
    @RequestMapping
    public ListDto<ArtistDto> findAll(Pageable pageable) {
        return artistListDtoFactory.build(artistBoundaryService.findAllArtists(pageable));
    }

    /**
     * GET request to retrieve a single artist by identifier.
     * @param identifier the identifier of the artist
     * @return the artist or a 404
     */
    @RequestMapping(path = PATH_VARIABLE_ID)
    public ResponseEntity<?> findOneByIdentifier(@PathVariable(VARIABLE_ID) UUID identifier) {
        Artist artist = artistBoundaryService.findOneByIdentifier(identifier);
        if (artist == null) {
            return ResponseEntity.notFound().build();
        } else {
            return new ResponseEntity<>(artistDtoFactory.build(artist), HttpStatus.OK);
        }
    }

    @RequestMapping(method = RequestMethod.POST)
    public ResponseEntity<?> createArtist(@RequestBody CreateArtistDto body) {
        Artist createdArtist = artistBoundaryService.createArtist(body.getName(), body.getMbid());
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.setLocation(ServletUriComponentsBuilder.fromCurrentRequest().path(PATH_VARIABLE_ID)
                .buildAndExpand(createdArtist.getIdentifier()).toUri());
        return new ResponseEntity<>(artistDtoFactory.build(createdArtist), httpHeaders, HttpStatus.CREATED);
    }

}