package org.rliz.cfm.release.api.dto;

import org.rliz.cfm.common.api.dto.AbstractDto;
import org.rliz.cfm.release.model.ReleaseGroup;

import java.util.UUID;

/**
 * Represents a {@link ReleaseGroup} on the wire.
 */
public class ReleaseGroupDto extends AbstractDto<ReleaseGroup> {

    public ReleaseGroupDto(ReleaseGroup data) {
        super(data);
    }

    public UUID getIdentifier() {
        return data.getIdentifier();
    }

    public String getTitle() {
        return data.getTitle();
    }

    public UUID getMbid() {
        return data.getMbid();
    }
}
