package cfm.artist.model;

import cfm.common.model.AbstractEntity;

import javax.persistence.Column;
import javax.persistence.Entity;

@Entity
public class Artist extends AbstractEntity {

    @Column
    public String name;

}
