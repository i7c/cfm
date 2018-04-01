package org.rliz.mbs.area.model

import org.rliz.mbs.common.model.FirstClassEntity

import javax.persistence.Column
import javax.persistence.Entity
import javax.persistence.Index
import javax.persistence.Table

@Entity
@Table(
    name = "area",
    indexes = [(Index(name = "area_pkey", columnList = "id")), (Index(
        name = "area_idx_gid",
        columnList = "gid"
    )), (Index(name = "area_idx_name", columnList = "name"))]
)
class Area : FirstClassEntity() {

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

    @Column(name = "name")
    val name: String? = null

    @Column(name = "comment")
    val comment: String? = null
}
