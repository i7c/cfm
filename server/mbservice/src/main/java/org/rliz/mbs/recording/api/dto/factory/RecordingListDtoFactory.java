package org.rliz.mbs.recording.api.dto.factory;

import org.rliz.mbs.common.api.dto.ListDto;
import org.rliz.mbs.recording.api.dto.RecordingDto;
import org.rliz.mbs.recording.data.Recording;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Factory for {@link ListDto}s containing {@link RecordingDto}s.
 */
@Component
public class RecordingListDtoFactory {

    private RecordingDtoFactory recordingDtoFactory;

    @Autowired
    public RecordingListDtoFactory(RecordingDtoFactory recordingDtoFactory) {
        this.recordingDtoFactory = recordingDtoFactory;
    }

    /**
     * Build a {@link ListDto} of {@link RecordingDto}s.
     *
     * @param recordings the recordings to represent in wire format
     * @return the list dto
     */
    public ListDto<RecordingDto> build(Page<Recording> recordings) {
        List<RecordingDto> recordingDtos = recordings.getContent().stream()
                .map(recordingDtoFactory::build)
                .collect(Collectors.toList());
        return new ListDto<>(recordingDtos, recordings);
    }
}
