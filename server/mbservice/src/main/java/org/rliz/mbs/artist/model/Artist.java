package org.rliz.mbs.artist.model;

import org.rliz.mbs.area.model.Area;
import org.rliz.mbs.common.model.FirstClassEntity;

import javax.persistence.*;
import java.util.Date;

/**
 * An Artist as stored in the musicbrainz db.
 */
@Entity
@Table(
        name = "artist",
        indexes = {
                @Index(name = "artist_pkey", columnList = "id"),
                @Index(name = "artist_idx_gid", columnList = "gid"),
                @Index(name = "artist_idx_null_comment", columnList = "name"),
                @Index(name = "artist_idx_uniq_name_comment", columnList = "name, comment"),
                @Index(name = "artist_idx_area", columnList = "area"),
                @Index(name = "artist_idx_begin_area", columnList = "begin_area"),
                @Index(name = "artist_idx_end_area", columnList = "end_area"),
                @Index(name = "artist_idx_lower_name", columnList = "name"),
                @Index(name = "artist_idx_name", columnList = "name"),
                @Index(name = "artist_idx_sort_name", columnList = "sort_name"),
        }
)
public class Artist extends FirstClassEntity {

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

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "type")
    private ArtistType artistType;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "area")
    private Area area;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "begin_area")
    private Area beginArea;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "end_area")
    private Area endArea;

    @Column(name = "last_updated")
    @Temporal(TemporalType.TIMESTAMP)
    private Date lastUpdated;

    // Not mapped:
//    gender           | integer
//    edits_pending    | integer

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

    public ArtistType getArtistType() {
        return artistType;
    }

    public Area getArea() {
        return area;
    }

    public Area getBeginArea() {
        return beginArea;
    }

    public Area getEndArea() {
        return endArea;
    }

    public Date getLastUpdated() {
        return lastUpdated;
    }

    @Override
    public String getDisplayName() {
        return getName();
    }
}
