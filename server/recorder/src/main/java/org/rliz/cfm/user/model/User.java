package org.rliz.cfm.user.model;

import org.rliz.cfm.common.model.AbstractEntity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;
import javax.validation.constraints.NotNull;

/**
 * Represents a cfm user.
 */
@Entity
@Table(uniqueConstraints = @UniqueConstraint(columnNames = "username"), name = "cfm_user")
public class User extends AbstractEntity {

    @NotNull
    @Column(length = 128, name = "username")
    private String username;

    @NotNull
    @Column(length = 128)
    private String password;

    @Column(length = 128)
    private String apiKey;

    /**
     * Default constructor.
     */
    public User() {
        // for JPA
    }

    /**
     * Constructor.
     *
     * @param username username
     * @param password password
     * @param apiKey   api key that can be used as an alternative for password
     */
    public User(String username, String password, String apiKey) {
        this.username = username;
        this.password = password;
        this.apiKey = apiKey;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getApiKey() {
        return apiKey;
    }

    public void setApiKey(String apiKey) {
        this.apiKey = apiKey;
    }

    @Override
    public String getDisplayName() {
        return username;
    }
}
