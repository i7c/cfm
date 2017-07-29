package org.rliz.cfm.user.model;

import org.rliz.cfm.common.model.AbstractEntity;

import javax.persistence.*;
import java.util.List;

/**
 * Represents a cfm user.
 */
@Entity
@Table(uniqueConstraints = @UniqueConstraint(columnNames = "username"), name = "cfm_user")
public class User extends AbstractEntity {

    @Column(length = 128, name = "username")
    public String username;

    @Column(length = 128)
    public String password;

    @OneToMany(mappedBy = "user")
    public List<ApiKey> apiKeys;

    public User() {
        // for JPA
    }

    public User(String username, String password) {
        this.username = username;
        this.password = password;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        User user = (User) o;

        return username.equals(user.username);
    }

    @Override
    public int hashCode() {
        return username.hashCode();
    }

    @Override
    public String getDisplayName() {
        return username;
    }
}
