package org.rliz.cfm.artist.api.dto.factory;

import org.rliz.cfm.artist.api.ArtistController;
import org.rliz.cfm.artist.api.dto.ArtistDto;
import org.rliz.cfm.artist.model.Artist;
import org.springframework.hateoas.mvc.ControllerLinkBuilder;
import org.springframework.stereotype.Component;

import static org.springframework.hateoas.mvc.ControllerLinkBuilder.methodOn;

/**
 * Builds {@link ArtistDto}s from {@link Artist} objects.
 */
@Component
public class ArtistDtoFactory {

    public ArtistDto build(Artist artist) {
        ArtistDto dto = new ArtistDto(artist);
        dto.add(ControllerLinkBuilder.linkTo(methodOn(ArtistController.class)
                .findOneByIdentifier(artist.identifier)).withSelfRel());
        return dto;
    }

}
