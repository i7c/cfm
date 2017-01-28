package org.rliz.cfm.release.api.dto.factory;

import org.rliz.cfm.release.api.dto.ReleaseGroupDto;
import org.rliz.cfm.release.model.ReleaseGroup;
import org.springframework.stereotype.Component;

/**
 * Factory for {@link ReleaseGroupDto}s.
 */
@Component
public class ReleaseGroupDtoFactory {

    public ReleaseGroupDto build(ReleaseGroup releaseGroup) {
        return new ReleaseGroupDto(releaseGroup);
    }
}
