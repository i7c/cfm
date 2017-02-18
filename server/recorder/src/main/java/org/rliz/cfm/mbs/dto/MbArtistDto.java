package org.rliz.cfm.mbs.dto;

/**
 * DTO for artists from the musicbrainz service.
 */
public class MbArtistDto extends AbstractMbDto {

    private String name;

    private String sortName;

    private Integer beginDateYear;

    private Integer beginDateMonth;

    private Integer beginDateDay;

    private Integer endDateYear;

    private Integer endDateMonth;

    private Integer endDateDay;

    private Boolean ended;

    private String comment;

    public String getName() {
        return name;
    }

    public String getSortName() {
        return sortName;
    }

    public Integer getBeginDateYear() {
        return beginDateYear;
    }

    public Integer getBeginDateMonth() {
        return beginDateMonth;
    }

    public Integer getBeginDateDay() {
        return beginDateDay;
    }

    public Integer getEndDateYear() {
        return endDateYear;
    }

    public Integer getEndDateMonth() {
        return endDateMonth;
    }

    public Integer getEndDateDay() {
        return endDateDay;
    }

    public Boolean getEnded() {
        return ended;
    }

    public String getComment() {
        return comment;
    }
}
