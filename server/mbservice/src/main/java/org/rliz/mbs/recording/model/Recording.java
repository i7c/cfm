package org.rliz.mbs.recording.model;

import org.rliz.mbs.common.model.AbstractEntity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import java.util.Date;

/**
 * Created by cmw on 09/02/17.
 */
@Entity
@Table(name = "recording")
public class Recording extends AbstractEntity {

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

//    Not mapped yet:
//    artist_credit | integer
//    edits_pending | integer

}
