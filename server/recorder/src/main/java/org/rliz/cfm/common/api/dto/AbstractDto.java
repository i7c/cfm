package org.rliz.cfm.common.api.dto;

import org.rliz.cfm.common.model.AbstractEntity;
import org.springframework.hateoas.ResourceSupport;

import java.util.UUID;

/**
 * Abstract base class for DTOs.
 */
public abstract class AbstractDto<T extends AbstractEntity> extends ResourceSupport {

    protected T data;

    public AbstractDto(T data) {
        this.data = data;
    }

    public UUID getIdentifier() {
        return data.getIdentifier();
    }
}
