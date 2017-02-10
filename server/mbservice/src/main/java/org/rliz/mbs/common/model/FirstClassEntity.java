package org.rliz.mbs.common.model;

import javax.persistence.Column;
import javax.persistence.MappedSuperclass;
import javax.validation.constraints.NotNull;
import java.util.UUID;

/**
 * Represents an entity that has a {@link UUID}.
 */
@MappedSuperclass
public class FirstClassEntity extends AbstractEntity {

    @NotNull
    @Column(name = "gid")
    private UUID identifier;

    public UUID getIdentifier() {
        return identifier;
    }

    public void setIdentifier(UUID identifier) {
        this.identifier = identifier;
    }
}
