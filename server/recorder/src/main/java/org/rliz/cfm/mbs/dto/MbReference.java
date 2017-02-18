package org.rliz.cfm.mbs.dto;

import java.util.UUID;

/**
 * Reference to another entity.
 */
public class MbReference {

    private String displayName;

    private UUID identifier;

    public String getDisplayName() {
        return displayName;
    }

    public UUID getIdentifier() {
        return identifier;
    }
}
