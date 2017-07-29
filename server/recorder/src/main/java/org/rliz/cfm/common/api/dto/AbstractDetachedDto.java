package org.rliz.cfm.common.api.dto;

import org.rliz.cfm.common.model.AbstractEntity;

import java.util.UUID;

/**
 * Base class for DTOs that do not retain a relation to the represented entity.
 */
public class AbstractDetachedDto {

    public UUID identifier;

    public AbstractDetachedDto(AbstractEntity entity) {
        identifier = entity.getIdentifier();
    }
}
