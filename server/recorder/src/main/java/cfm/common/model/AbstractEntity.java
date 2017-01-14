package cfm.common.model;

import javax.persistence.Entity;
import javax.persistence.Id;
import java.util.UUID;

/**
 * Abstract entity which contains fields common to all entities.
 */
@Entity
public abstract class AbstractEntity {

    @Id
    public Long oid;

    public UUID identifier;
}
