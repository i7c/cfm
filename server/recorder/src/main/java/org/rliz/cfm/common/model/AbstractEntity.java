package org.rliz.cfm.common.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.validation.constraints.NotNull;
import java.util.UUID;

/**
 * Abstract entity which contains fields common to all entities.
 */
@Entity
public abstract class AbstractEntity {

    @Id
    @GeneratedValue
    @Column(name = "oid")
    public Long oid;

    public UUID identifier;
}
