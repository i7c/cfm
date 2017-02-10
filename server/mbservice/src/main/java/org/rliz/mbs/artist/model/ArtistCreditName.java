package org.rliz.mbs.artist.model;

import javax.persistence.*;

/**
 * Represents artist credit name.
 */
@Entity
@Table(name = "artist_credit_name")
public class ArtistCreditName {

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "artist_credit", referencedColumnName = "id")
    public ArtistCredit artistCredit;

    @Column(name = "position")
    public Integer position;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "artist", referencedColumnName = "id")
    public Artist artist;

    @Column(name = "name")
    private String name;

    @Column(name = "join_phrase")
    private String joinPhrase;

}
