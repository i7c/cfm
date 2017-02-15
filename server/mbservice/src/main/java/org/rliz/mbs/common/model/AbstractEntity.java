package org.rliz.mbs.common.model;

import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.MappedSuperclass;
import javax.validation.constraints.NotNull;
import java.util.UUID;

/**
 * Common base class for entities in the musicbrainz database.
 */
@MappedSuperclass
public abstract class AbstractEntity {

    @Id
    @Column(name = "id")
    private Long oid;

    public Long getOid() {
        return oid;
    }
}
