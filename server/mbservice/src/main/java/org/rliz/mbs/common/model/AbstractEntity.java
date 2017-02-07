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
public class AbstractEntity {

    @Id
    @Column(name = "id")
    private Long oid;

    @NotNull
    @Column(name = "gid")
    private UUID identifier;

    public Long getOid() {
        return oid;
    }

    public UUID getIdentifier() {
        return identifier;
    }
}
