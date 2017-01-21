package org.rliz.cfm.common.api.dto;

import org.rliz.cfm.common.model.AbstractEntity;

import java.util.UUID;

/**
 * Created by cmw on 21/01/17.
 */
public class Reference<T extends AbstractEntity> {

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
