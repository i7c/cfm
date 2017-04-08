package org.rliz.mbs.area.model;

import org.rliz.mbs.common.model.FirstClassEntity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Index;
import javax.persistence.Table;

/**
 * Represents an geographic area as modelled by musicbrainz.
 */
@Entity
@Table(
        name = "area",
        indexes = {
                @Index(name = "area_pkey", columnList = "id"),
                @Index(name = "area_idx_gid", columnList = "gid"),
                @Index(name = "area_idx_name", columnList = "name")
        }
)
public class Area extends FirstClassEntity {

    @Column(name = "name")
    private String name;

    @Column(name = "comment")
    private String comment;

    // Not mapped:
//    type             | integer
//    edits_pending    | integer
//    last_updated     | timestamp with time zone
//    begin_date_year  | smallint
//    begin_date_month | smallint
//    begin_date_day   | smallint
//    end_date_year    | smallint
//    end_date_month   | smallint
//    end_date_day     | smallint
//    ended            | boolean

    public String getName() {
        return name;
    }

    public String getComment() {
        return comment;
    }

    @Override
    public String getDisplayName() {
        return getName();
    }
}
