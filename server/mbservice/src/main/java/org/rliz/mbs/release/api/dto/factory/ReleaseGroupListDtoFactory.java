package org.rliz.mbs.release.api.dto.factory;

import org.rliz.mbs.common.api.dto.ListDto;
import org.rliz.mbs.release.api.dto.ReleaseGroupDto;
import org.rliz.mbs.release.model.ReleaseGroup;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Creates {@link ListDto}s for {@link ReleaseGroupDto}s.
 */
@Component
public class ReleaseGroupListDtoFactory {

    private ReleaseGroupDtoFactory releaseGroupDtoFactory;

    public ReleaseGroupListDtoFactory(ReleaseGroupDtoFactory releaseGroupDtoFactory) {
        this.releaseGroupDtoFactory = releaseGroupDtoFactory;
    }

    /**
     * Build a {@link ListDto} for a list of {@link ReleaseGroup}s.
     *
     * @param releaseGroups the release groups
     * @return the list dto
     */
    public ListDto<ReleaseGroupDto> build(Page<ReleaseGroup> releaseGroups) {
        List<ReleaseGroupDto> releaseGroupDtos = releaseGroups.getContent()
                .stream()
                .map(releaseGroupDtoFactory::build)
                .collect(Collectors.toList());
        return new ListDto<ReleaseGroupDto>(releaseGroupDtos, releaseGroups);
    }
}
