package org.rliz.mbs.artist.model;

import javax.persistence.*;
import java.io.Serializable;

/**
 * Represents artist credit name.
 */
@Entity
@Table(
        name = "artist_credit_name",
        indexes = {
                @Index(name = "ix_acn_acp", columnList = "artist_credit, position"),
                @Index(name = "ix_acn_artist", columnList = "artist"),
                @Index(name = "ix_acn_artistcredit", columnList = "artist_credit")
        }
)
public class ArtistCreditName implements Serializable {

    @Id
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "artist_credit", referencedColumnName = "id")
    private ArtistCredit artistCredit;

    @Id
    @Column(name = "position")
    private Integer position;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "artist", referencedColumnName = "id")
    private Artist artist;

    @Column(name = "name")
    private String name;

    @Column(name = "join_phrase")
    private String joinPhrase;

    public ArtistCredit getArtistCredit() {
        return artistCredit;
    }

    public void setArtistCredit(ArtistCredit artistCredit) {
        this.artistCredit = artistCredit;
    }

    public Integer getPosition() {
        return position;
    }

    public void setPosition(Integer position) {
        this.position = position;
    }

    public Artist getArtist() {
        return artist;
    }

    public void setArtist(Artist artist) {
        this.artist = artist;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getJoinPhrase() {
        return joinPhrase;
    }

    public void setJoinPhrase(String joinPhrase) {
        this.joinPhrase = joinPhrase;
    }
}
