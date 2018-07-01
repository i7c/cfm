package org.rliz.cfm.recorder.common.data

import org.springframework.jdbc.support.JdbcUtils
import java.sql.ResultSet
import java.util.UUID

/**
 * Extract a column by label from a result set and use Kotlin/Java "null" value for NULL data.
 * E.g. this can be used to fetch NULL values of a long column (instead of getting 0).
 *
 * This method is *NOT* thread-safe on the underlying ResultSet.
 */
inline fun <reified T> ResultSet.getNullable(label: String): T? =
    JdbcUtils.getResultSetValue(this, this.findColumn(label), T::class.java)
        .takeUnless { wasNull() } as T

fun Int.assertAffectedRows(expected: Int): Int =
    if (this != expected)
        throw IllegalStateException("Expected $expected affected rows, but was $this")
    else this

fun String?.uuid(): UUID? = if (this == null || this.isBlank()) null else UUID.fromString(this)
