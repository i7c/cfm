package org.rliz.mbs.common.model

import java.util.UUID
import javax.persistence.Column
import javax.persistence.MappedSuperclass
import javax.validation.constraints.NotNull

/**
 * Represents an entity that has a [UUID].
 */
@MappedSuperclass
abstract class FirstClassEntity : AbstractEntity() {
    @NotNull
    @Column(name = "gid")
    var identifier: UUID? = null
}
