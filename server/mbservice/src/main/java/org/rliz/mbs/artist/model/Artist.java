package org.rliz.mbs.artist.model;

import org.rliz.mbs.common.model.AbstractEntity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

/**
 * An Artist as stored in the musicbrainz db.
 */
@Entity
@Table(name = "artist")
public class Artist extends AbstractEntity {

    @Column(name = "name")
    private String name;

    @Column(name = "sort_name")
    private String sortName;

    @Column(name = "begin_date_year")
    private Integer beginDateYear;

    @Column(name = "begin_date_month")
    private Integer beginDateMonth;

    @Column(name = "begin_date_day")
    private Integer beginDateDay;

    @Column(name = "end_date_year")
    private Integer endDateYear;

    @Column(name = "end_date_month")
    private Integer endDateMonth;

    @Column(name = "end_date_day")
    private Integer endDateDay;

    @Column(name = "ended")
    private Boolean ended;

    @Column(name = "comment")
    private String comment;

    // Not mapped yet:
//    type             | integer
//    area             | integer
//    gender           | integer
//    edits_pending    | integer
//    last_updated     | timestamp with time zone
//    begin_area       | integer
//    end_area         | integer


    @Override
    public String toString() {
        return "Artist{" +
                "name='" + name + '\'' +
                ", sortName='" + sortName + '\'' +
                ", beginDateYear=" + beginDateYear +
                ", beginDateMonth=" + beginDateMonth +
                ", beginDateDay=" + beginDateDay +
                ", endDateYear=" + endDateYear +
                ", endDateMonth=" + endDateMonth +
                ", endDateDay=" + endDateDay +
                ", ended=" + ended +
                ", comment='" + comment + '\'' +
                '}';
    }

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
