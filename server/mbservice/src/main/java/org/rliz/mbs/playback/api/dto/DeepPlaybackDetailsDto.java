package org.rliz.mbs.playback.api.dto;

import org.rliz.mbs.recording.data.Recording;
import org.rliz.mbs.release.model.ReleaseGroup;

public class DeepPlaybackDetailsDto {

    private DeepRecordingDto deepRecordingDto;

    private DeepReleaseGroupDto deepReleaseGroupDto;

    public DeepPlaybackDetailsDto(Recording recording, ReleaseGroup releaseGroup) {
        this.deepRecordingDto = new DeepRecordingDto(recording);
        this.deepReleaseGroupDto = new DeepReleaseGroupDto(releaseGroup);
    }

    public DeepRecordingDto getRecording() {
        return deepRecordingDto;
    }

    public DeepReleaseGroupDto getReleaseGroup() {
        return deepReleaseGroupDto;
    }
}
