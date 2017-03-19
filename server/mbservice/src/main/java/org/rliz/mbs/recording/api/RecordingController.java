package org.rliz.mbs.recording.api;

import org.rliz.mbs.common.api.dto.ListDto;
import org.rliz.mbs.recording.api.dto.RecordingDto;
import org.rliz.mbs.recording.api.dto.factory.RecordingDtoFactory;
import org.rliz.mbs.recording.api.dto.factory.RecordingListDtoFactory;
import org.rliz.mbs.recording.boundary.RecordingBoundaryService;
import org.rliz.mbs.recording.model.Recording;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

/**
 * REST API endpoint for {@link Recording}s.
 */
@RestController
@RequestMapping("/api/v1/recordings")
public class RecordingController {

    private RecordingBoundaryService recordingBoundaryService;

    private RecordingDtoFactory recordingDtoFactory;

    private RecordingListDtoFactory recordingListDtoFactory;

    @Autowired
    public RecordingController(RecordingBoundaryService recordingBoundaryService,
                               RecordingDtoFactory recordingDtoFactory,
                               RecordingListDtoFactory recordingListDtoFactory) {
        this.recordingBoundaryService = recordingBoundaryService;
        this.recordingDtoFactory = recordingDtoFactory;
        this.recordingListDtoFactory = recordingListDtoFactory;
    }

    /**
     * GET request to retrieve a {@link Recording} by identifier.
     *
     * @param identifier unique identifier
     * @return the {@link Recording}
     */
    @Transactional(readOnly = true)
    @RequestMapping("/{identifier}")
    public RecordingDto findByIdentifier(@PathVariable("identifier") UUID identifier) {
        Recording foundRecording = recordingBoundaryService.findByIdentifier(identifier);
        return recordingDtoFactory.build(foundRecording);
    }

    /**
     * GET request to find recordings on the given release group with given title.
     *
     * @param releaseGroupId the release group on which this Recording apppears
     * @param title          the title of the recording
     * @param pageable       page request
     * @return sorted list of recordings, best matching first
     */
    @Transactional(readOnly = true)
    @RequestMapping(method = RequestMethod.GET)
    public ListDto<RecordingDto> findByReleaseGroupIdAndName(@RequestParam("releaseGroupId") UUID releaseGroupId,
                                                             @RequestParam("title") String title,
                                                             Pageable pageable) {
        Page<Recording> foundRecordings =
                recordingBoundaryService.findRecordingByReleaseGroupIdentifierAndName(releaseGroupId, title, pageable);
        return recordingListDtoFactory.build(foundRecordings);
    }
}
