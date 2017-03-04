package org.rliz.cfm.playback.model;

import org.hibernate.validator.constraints.Range;
import org.rliz.cfm.common.model.AbstractEntity;
import org.rliz.cfm.playback.api.dto.CreatePlaybackDto;
import org.rliz.cfm.recording.model.Recording;
import org.rliz.cfm.release.model.ReleaseGroup;
import org.rliz.cfm.user.model.User;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.Date;
import java.util.List;
import java.util.UUID;

/**
 * Represents the playback of a single recording. This entity also stores the original information sent by the user
 * such that no data is lost.
 */
@Entity
@Table(
        indexes = {
                @Index(name = "ix_playback_oid", columnList = "oid"),
                @Index(name = "ix_playback_identifier", columnList = "identifier"),
                @Index(name = "ix_playback_user", columnList = "user_oid")
        },
        uniqueConstraints = {
                @UniqueConstraint(name = "uc_playback_identifier", columnNames = "identifier")
        }
)
public class Playback extends AbstractEntity {

    @ManyToOne
    @JoinColumn(foreignKey = @ForeignKey(name = "FK_playback_recording"))
    private Recording recording;

    @ManyToOne
    @JoinColumn(foreignKey = @ForeignKey(name = "FK_playback_releasegroup"))
    private ReleaseGroup releaseGroup;

    @ManyToOne(optional = false)
    @JoinColumn(foreignKey = @ForeignKey(name = "FK_playback_user"))
    @NotNull
    private User user;

    @Temporal(TemporalType.TIMESTAMP)
    @NotNull
    private Date time;

    /*
        Originally sent data
     */
    private UUID originalMbTrackId;

    private UUID originalMbReleaseGroupId;

    @ElementCollection(fetch = FetchType.EAGER)
    @CollectionTable(foreignKey = @ForeignKey(name = "FK_playback_originalArtists"))
    private List<String> originalArtists;

    @Column(length = 512)
    private String originalTitle;

    @Column(length = 512)
    private String originalAlbum;

    @Range(min = 0L)
    private Long originalLength;

    @Range(min = 0L)
    private Integer originalDiscNumber;

    @Range(min = 0L)
    private Integer originalTrackNumber;

    @Range(min = 1L)
    private Long playTime;

    /**
     * Default constructor.
     */
    public Playback() {
        // For JPA
    }

    /**
     * Constructor.
     *
     * @param recording    the played recording, must be non-null
     * @param releaseGroup the associated release group (album), can be null
     * @param time         the time of playback
     */
    public Playback(Recording recording, ReleaseGroup releaseGroup, User user, Date time) {
        this.recording = recording;
        this.releaseGroup = releaseGroup;
        this.user = user;
        this.time = time;
    }

    /**
     * Set all fields that track data originally sent by the user.
     *
     * @param dto the dto sent by the user
     */
    public void setOriginalDataFromDto(CreatePlaybackDto dto) {
        this.setOriginalMbTrackId(dto.getMbTrackId());
        this.setOriginalMbReleaseGroupId(dto.getMbReleaseGroupId());
        this.setOriginalArtists(dto.getArtists());
        this.setOriginalTitle(dto.getTitle());
        this.setOriginalAlbum(dto.getAlbum());
        this.setOriginalLength(dto.getLength());
        this.setOriginalDiscNumber(dto.getDiscNumber());
        this.setOriginalTrackNumber(dto.getTrackNumber());
        this.setPlayTime(dto.getPlayTime());
    }

    public Recording getRecording() {
        return recording;
    }

    public void setRecording(Recording recording) {
        this.recording = recording;
    }

    public ReleaseGroup getReleaseGroup() {
        return releaseGroup;
    }

    public void setReleaseGroup(ReleaseGroup releaseGroup) {
        this.releaseGroup = releaseGroup;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Date getTime() {
        return time;
    }

    public void setTime(Date time) {
        this.time = time;
    }

    public UUID getOriginalMbTrackId() {
        return originalMbTrackId;
    }

    public void setOriginalMbTrackId(UUID originalMbTrackId) {
        this.originalMbTrackId = originalMbTrackId;
    }

    public UUID getOriginalMbReleaseGroupId() {
        return originalMbReleaseGroupId;
    }

    public void setOriginalMbReleaseGroupId(UUID originalMbReleaseGroupId) {
        this.originalMbReleaseGroupId = originalMbReleaseGroupId;
    }

    public List<String> getOriginalArtists() {
        return originalArtists;
    }

    public void setOriginalArtists(List<String> originalArtists) {
        this.originalArtists = originalArtists;
    }

    public String getOriginalTitle() {
        return originalTitle;
    }

    public void setOriginalTitle(String originalTitle) {
        this.originalTitle = originalTitle;
    }

    public String getOriginalAlbum() {
        return originalAlbum;
    }

    public void setOriginalAlbum(String originalAlbum) {
        this.originalAlbum = originalAlbum;
    }

    public Long getOriginalLength() {
        return originalLength;
    }

    public void setOriginalLength(Long originalLength) {
        this.originalLength = originalLength;
    }

    public Integer getOriginalDiscNumber() {
        return originalDiscNumber;
    }

    public void setOriginalDiscNumber(Integer originalDiscNumber) {
        this.originalDiscNumber = originalDiscNumber;
    }

    public Integer getOriginalTrackNumber() {
        return originalTrackNumber;
    }

    public void setOriginalTrackNumber(Integer originalTrackNumber) {
        this.originalTrackNumber = originalTrackNumber;
    }

    public Long getPlayTime() {
        return playTime;
    }

    public void setPlayTime(Long playTime) {
        this.playTime = playTime;
    }

    @Override
    public String getDisplayName() {
        return "Playback " + String.valueOf(getIdentifier());
    }
}
