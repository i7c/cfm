package org.rliz.cfm.playback.api.dto;

import java.util.UUID;

/**
 * DTO that represents creation of a playback.
 */
public class CreatePlaybackDto  {

    private UUID mbTrackId;

    private UUID mbReleaseGroupId;

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
}
