package org.rliz.cfm.recorder.common.data

import org.springframework.jdbc.support.JdbcUtils
import java.sql.ResultSet

/**
 * Extract a column by label from a result set and use Kotlin/Java "null" value for NULL data.
 * E.g. this can be used to fetch NULL values of a long column (instead of getting 0).
 *
 * This method is *NOT* thread-safe on the underlying ResultSet.
 */
inline fun <reified T> ResultSet.getNullable(label: String): T? =
    JdbcUtils.getResultSetValue(this, this.findColumn(label), T::class.java)
        .let { if (this.wasNull()) null else it as T }
