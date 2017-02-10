package org.rliz.mbs.recording.model;

import org.rliz.mbs.artist.model.ArtistCredit;
import org.rliz.mbs.common.model.AbstractEntity;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.Date;
import java.util.UUID;

/**
 * Represents a musicbrainz Recording.
 */
@Entity
@Table(name = "recording")
public class Recording extends AbstractEntity {

    @NotNull
    @Column(name = "gid")
    private UUID identifier;

    @Column(name = "name")
    private String name;

    @Column(name = "length")
    private Long length;

    @Column(name = "comment")
    private String comment;

    @Column(name = "last_updated")
    private Date lastUpdated;

    @Column(name = "video")
    public Boolean video;

    @ManyToOne
    @JoinColumn(name = "artist_credit", referencedColumnName = "id")
    public ArtistCredit artistCredit;

//    Not mapped yet:
//    edits_pending | integer

}
