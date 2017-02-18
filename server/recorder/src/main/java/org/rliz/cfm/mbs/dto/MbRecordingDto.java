package org.rliz.cfm.mbs.dto;

import java.util.Date;
import java.util.List;

/**
 * DTO for a musicbrainz Recording.
 */
public class MbRecordingDto extends AbstractMbDto {

    private String name;

    private Long length;

    private String comment;

    private Date lastUpdated;

    private Boolean video;

    private List<MbReference> artistReferences;

    public String getName() {
        return name;
    }

    public Long getLength() {
        return length;
    }

    public String getComment() {
        return comment;
    }

    public Date getLastUpdated() {
        return lastUpdated;
    }

    public Boolean getVideo() {
        return video;
    }

    public List<MbReference> getArtistReferences() {
        return artistReferences;
    }
}
