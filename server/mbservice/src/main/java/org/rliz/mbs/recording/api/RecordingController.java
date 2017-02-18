package org.rliz.mbs.recording.api;

import org.rliz.mbs.recording.api.dto.RecordingDto;
import org.rliz.mbs.recording.api.dto.factory.RecordingDtoFactory;
import org.rliz.mbs.recording.boundary.RecordingBoundaryService;
import org.rliz.mbs.recording.model.Recording;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

/**
 * REST API endpoint for {@link Recording}s.
 */
@RestController
@RequestMapping("/api/v1/recordings")
public class RecordingController {

    private RecordingBoundaryService recordingBoundaryService;

    private RecordingDtoFactory recordingDtoFactory;

    @Autowired
    public RecordingController(RecordingBoundaryService recordingBoundaryService,
                               RecordingDtoFactory recordingDtoFactory) {
        this.recordingBoundaryService = recordingBoundaryService;
        this.recordingDtoFactory = recordingDtoFactory;
    }

    /**
     * GET request to retrieve a {@link Recording} by identifier.
     * @param identifier unique identifier
     * @return the {@link Recording}
     */
    @RequestMapping("/{identifier}")
    public RecordingDto findByIdentifier(@PathVariable("identifier") UUID identifier) {
        Recording foundRecording = recordingBoundaryService.findByIdentifier(identifier);
        return recordingDtoFactory.build(foundRecording);
    }
}
