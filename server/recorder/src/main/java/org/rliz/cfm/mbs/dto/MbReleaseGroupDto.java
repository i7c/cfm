package org.rliz.cfm.mbs.dto;

import java.util.Date;
import java.util.List;

/**
 * DTO for musicbrainz release groups.
 */
public class MbReleaseGroupDto extends AbstractMbDto {

    private String name;

    private List<MbReference> artistReferences;

    private String comment;

    private Date lastUpdated;

    public String getName() {
        return name;
    }

    public List<MbReference> getArtistReferences() {
        return artistReferences;
    }

    public String getComment() {
        return comment;
    }

    public Date getLastUpdated() {
        return lastUpdated;
    }
}
