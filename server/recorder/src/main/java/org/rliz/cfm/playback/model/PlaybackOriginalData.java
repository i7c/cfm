package org.rliz.cfm.playback.model;

import org.hibernate.validator.constraints.Range;
import org.rliz.cfm.playback.api.dto.CreatePlaybackDto;

import javax.persistence.*;
import java.util.List;
import java.util.UUID;

/**
 * Represents whatever data the client sent originally in order to create a {@link Playback}.
 */
@Embeddable
public class PlaybackOriginalData {

    private UUID mbTrackId;

    private UUID mbReleaseGroupId;

    @ElementCollection(fetch = FetchType.EAGER)
    @CollectionTable(foreignKey = @ForeignKey(name = "FK_playback_original_artists"))
    private List<String> artists;

    @Column(length = 512)
    private String title;

    @Column(length = 512)
    private String album;

    @Range(min = 0L)
    private Long length;

    @Range(min = 0L)
    private Integer discNumber;

    @Range(min = 0L)
    private Integer trackNumber;

    @Range(min = 1L)
    private Long playTime;

    /**
     * Creates a {@link PlaybackOriginalData} object from a given {@link CreatePlaybackDto} as received from the client.
     *
     * @param dto dto from the client
     * @return new {@link PlaybackOriginalData}
     */
    public static PlaybackOriginalData fromCreatePlaybackDto(CreatePlaybackDto dto) {
        PlaybackOriginalData originalData = new PlaybackOriginalData();
        originalData.setMbTrackId(dto.getMbTrackId());
        originalData.setMbReleaseGroupId(dto.getMbReleaseGroupId());
        originalData.setArtists(dto.getArtists());
        originalData.setTitle(dto.getTitle());
        originalData.setAlbum(dto.getAlbum());
        originalData.setLength(dto.getLength());
        originalData.setDiscNumber(dto.getDiscNumber());
        originalData.setTrackNumber(dto.getTrackNumber());
        originalData.setPlayTime(dto.getPlayTime());
        return originalData;
    }

    public UUID getMbTrackId() {
        return mbTrackId;
    }

    public void setMbTrackId(UUID mbTrackId) {
        this.mbTrackId = mbTrackId;
    }

    public UUID getMbReleaseGroupId() {
        return mbReleaseGroupId;
    }

    public void setMbReleaseGroupId(UUID mbReleaseGroupId) {
        this.mbReleaseGroupId = mbReleaseGroupId;
    }

    public List<String> getArtists() {
        return artists;
    }

    public void setArtists(List<String> artists) {
        this.artists = artists;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getAlbum() {
        return album;
    }

    public void setAlbum(String album) {
        this.album = album;
    }

    public Long getLength() {
        return length;
    }

    public void setLength(Long length) {
        this.length = length;
    }

    public Integer getDiscNumber() {
        return discNumber;
    }

    public void setDiscNumber(Integer discNumber) {
        this.discNumber = discNumber;
    }

    public Integer getTrackNumber() {
        return trackNumber;
    }

    public void setTrackNumber(Integer trackNumber) {
        this.trackNumber = trackNumber;
    }

    public Long getPlayTime() {
        return playTime;
    }

    public void setPlayTime(Long playTime) {
        this.playTime = playTime;
    }

}
