package org.rliz.mbs.common.model

import javax.persistence.Column
import javax.persistence.Id
import javax.persistence.MappedSuperclass

/**
 * Common base class for entities in the musicbrainz database.
 */
@MappedSuperclass
abstract class AbstractEntity {

    @Id
    @Column(name = "id")
    val oid: Long? = null
}
