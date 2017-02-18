package org.rliz.cfm.mbs.dto;

import java.util.UUID;

/**
 * Abstract base class for Musicbrainz DTOs.
 */
public abstract class AbstractMbDto {

    private UUID identifier;

    public UUID getIdentifier() {
        return identifier;
    }
}
