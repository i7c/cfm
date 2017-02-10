package org.rliz.mbs.common.api.dto;

import org.rliz.mbs.common.model.FirstClassEntity;
import org.springframework.hateoas.ResourceSupport;

import java.util.UUID;


/**
 * Abstract base class for DTOs.
 */
public abstract class AbstractDto<T extends FirstClassEntity> extends ResourceSupport {

    protected T data;

    public AbstractDto(T data) {
        this.data = data;
    }

    public UUID getIdentifier() {
        return data.getIdentifier();
    }
}
