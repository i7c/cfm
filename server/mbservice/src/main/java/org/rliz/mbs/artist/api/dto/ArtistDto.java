package org.rliz.mbs.artist.api.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import org.rliz.mbs.artist.model.Artist;
import org.rliz.mbs.common.api.dto.AbstractDto;

/**
 * DTO for {@link Artist}s.
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ArtistDto extends AbstractDto<Artist> {

    public ArtistDto(Artist data) {
        super(data);
    }

    public String getName() {
        return data.getName();
    }

    public String getSortName() {
        return data.getSortName();
    }

    public Integer getBeginDateYear() {
        return data.getBeginDateYear();
    }

    public Integer getBeginDateMonth() {
        return data.getBeginDateMonth();
    }

    public Integer getBeginDateDay() {
        return data.getBeginDateDay();
    }

    public Integer getEndDateYear() {
        return data.getEndDateYear();
    }

    public Integer getEndDateMonth() {
        return data.getEndDateMonth();
    }

    public Integer getEndDateDay() {
        return data.getEndDateDay();
    }

    public Boolean getEnded() {
        return data.getEnded();
    }

    public String getComment() {
        return data.getComment();
    }

    public String getType() { return data.getArtistType().getName(); }

    public String getAreaName() {
        if (data.getArea() != null) {
            return data.getArea().getName();
        } else {
            return null;
        }
    }

}
