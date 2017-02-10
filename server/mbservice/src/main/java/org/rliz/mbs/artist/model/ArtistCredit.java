package org.rliz.mbs.artist.model;

import org.rliz.mbs.common.model.AbstractEntity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import java.util.Date;

/**
 * Represents artist credit.
 */
@Entity
@Table(name = "artist_credit")
public class ArtistCredit extends AbstractEntity {

    @Column(name = "name")
    private String name;

    @Column(name = "artist_count")
    private Integer artistCount;

    @Column(name = "ref_count")
    private Integer refCount;

    @Column(name = "created")
    private Date created;

}
