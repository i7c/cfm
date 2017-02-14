package org.rliz.mbs.artist.model;

import org.rliz.mbs.common.model.AbstractEntity;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;
import java.util.List;
import java.util.Set;

/**
 * Represents artist credit.
 */
@Entity
@Table(name = "artist_credit", indexes = {
        @Index(name = "ix_artistcredit_id", columnList = "id")
})
public class ArtistCredit extends AbstractEntity implements Serializable {

    @Column(name = "name")
    private String name;

    @Column(name = "artist_count")
    private Integer artistCount;

    @Column(name = "ref_count")
    private Integer refCount;

    @Column(name = "created")
    private Date created;

    @OneToMany(mappedBy = "artistCredit", fetch = FetchType.LAZY)
    private Set<ArtistCreditName> artistCreditName;

    public String getName() {
        return name;
    }

    public Integer getArtistCount() {
        return artistCount;
    }

    public Integer getRefCount() {
        return refCount;
    }

    public Date getCreated() {
        return created;
    }

    public Set<ArtistCreditName> getArtistCreditName() {
        return artistCreditName;
    }

}
