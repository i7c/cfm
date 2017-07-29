package org.rliz.cfm.user.model;

import org.rliz.cfm.common.model.AbstractEntity;

import javax.persistence.*;
import javax.validation.constraints.NotNull;

/**
 * An API Key of a {@link User}.
 */
@Entity
public class ApiKey extends AbstractEntity {

    @NotNull
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(nullable = false)
    public User user;

    @NotNull
    @Column(nullable = false)
    public String key;

    public ApiKey(User user, String key) {
        this.user = user;
        this.key = key;
    }

    @Override
    public String getDisplayName() {
        return String.format("Api key of {}", user.getDisplayName());
    }
}
