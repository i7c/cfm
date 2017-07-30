package org.rliz.cfm.user.model;

import org.rliz.cfm.common.model.AbstractEntity;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.Date;

/**
 * Represents an invite for a new {@link User}.
 */
@Entity
@Table(indexes = {@Index(name = "IX_Invite_Identifier", columnList = "identifier")})
public class Invite extends AbstractEntity {

    @NotNull
    @ManyToOne(optional = false)
    @JoinColumn(nullable = false, foreignKey = @ForeignKey(name = "FK_Invite_Inviter"))
    public User inviter;

    @ManyToOne
    @JoinColumn(foreignKey = @ForeignKey(name = "FK_Invite_Invitee"))
    public User invitee;

    @NotNull
    @Temporal(TemporalType.TIMESTAMP)
    @Column(nullable = false)
    public Date date;

    public Invite(User inviter, Date date) {
        this.inviter = inviter;
        this.date = date;
    }

    @Override
    public String getDisplayName() {
        return "Invite";
    }
}
