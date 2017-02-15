package org.rliz.mbs.common.api.dto;


import org.rliz.mbs.common.model.FirstClassEntity;

import java.util.UUID;

/**
 * Reference to another resource.
 */
public class Reference<T extends FirstClassEntity> {

    private T data;

    public Reference(T data) {
        this.data = data;
    }

    public UUID getIdentifier() {
        return data.getIdentifier();
    }

    public String getDisplayName() {
        return data.getDisplayName();
    }

}
