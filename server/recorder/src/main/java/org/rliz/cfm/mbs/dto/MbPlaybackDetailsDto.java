package org.rliz.cfm.mbs.dto;

/**
 * DTO to receive details on a playback from the musicbrainz service.
 */
public class MbPlaybackDetailsDto {

    MbReference releaseReference;

    MbReference releaseGroupReference;

    MbReference recordingReference;

    MbReference recordingArtistsReference;

    MbReference releaseGroupArtistsReferences;

    public MbReference getReleaseReference() {
        return releaseReference;
    }

    public MbReference getReleaseGroupReference() {
        return releaseGroupReference;
    }

    public MbReference getRecordingReference() {
        return recordingReference;
    }

    public MbReference getRecordingArtistsReference() {
        return recordingArtistsReference;
    }

    public MbReference getReleaseGroupArtistsReferences() {
        return releaseGroupArtistsReferences;
    }
}
