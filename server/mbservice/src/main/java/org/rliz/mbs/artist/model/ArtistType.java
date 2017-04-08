package org.rliz.mbs.artist.model;

import org.rliz.mbs.common.model.FirstClassEntity;

import javax.persistence.*;

/**
 * Represents the type of an artist as modelled in the musicbrainz database.
 */
@Entity
@Table(
        name = "artist_type",
        indexes = {
                @Index(name = "artist_type_pkey", columnList = "id"),
                @Index(name = "artist_type_idx_gid", columnList = "gid")
        })
public class ArtistType extends FirstClassEntity {

        @Column(name = "name")
        private String name;

        @ManyToOne(fetch = FetchType.LAZY)
        @JoinColumn(name = "parent")
        private ArtistType parent;

        @Column(name = "child_order", nullable = false)
        private Integer childOrder;

        @Column(name = "description")
        private String description;

        @Override
        public String getDisplayName() {
                return getName();
        }

        public String getName() {
                return name;
        }

        public ArtistType getParent() {
                return parent;
        }

        public Integer getChildOrder() {
                return childOrder;
        }

        public String getDescription() {
                return description;
        }
}
