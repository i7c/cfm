package org.rliz.mbs.recording.api.dto.factory;

import org.rliz.mbs.recording.api.dto.RecordingDto;
import org.rliz.mbs.recording.model.Recording;
import org.springframework.stereotype.Component;

/**
 * Factory for {@link RecordingDto}s.
 */
@Component
public class RecordingDtoFactory {

    public RecordingDto build(Recording recording) {
        return new RecordingDto(recording);
    }
}
