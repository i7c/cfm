package org.rliz.cfm.artist.api;

import org.rliz.cfm.artist.api.dto.ArtistDto;
import org.rliz.cfm.artist.api.dto.CreateArtistDto;
import org.rliz.cfm.artist.api.dto.factory.ArtistListDtoFactory;
import org.rliz.cfm.artist.boundary.ArtistBoundaryService;
import org.rliz.cfm.artist.model.Artist;
import org.rliz.cfm.common.api.dto.ListDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

/**
 * REST controller for {@link org.rliz.cfm.artist.model.Artist}s.
 */
@RestController
@RequestMapping("/api/artists")
public class ArtistController {

    private final static String PATH_VARIABLE_ID = "/{artistId}";

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

    @RequestMapping(method = RequestMethod.POST)
    public ResponseEntity<?> createArtist(@RequestBody CreateArtistDto body) {
        Artist createdArtist = artistBoundaryService.createArtist(body.getName(), body.getMbid());
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.setLocation(ServletUriComponentsBuilder.fromCurrentRequest().path(PATH_VARIABLE_ID)
                .buildAndExpand(createdArtist.mbid).toUri());
        return new ResponseEntity<>(new ArtistDto(createdArtist), httpHeaders, HttpStatus.CREATED);
    }

}
