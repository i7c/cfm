package org.rliz.mbs.area.model

import java.util.UUID

class Area {

    val id: Long? = null

    val gid: UUID? = null

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

    val name: String? = null

    val comment: String? = null
}
