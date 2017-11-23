package org.rliz.mbs.recording.api

import org.rliz.mbs.common.api.toRes
import org.rliz.mbs.recording.boundary.RecordingBoundaryService
import org.rliz.mbs.recording.data.Recording
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.data.domain.Pageable
import org.springframework.transaction.annotation.Transactional
import org.springframework.web.bind.annotation.*
import java.util.*

@RestController
@RequestMapping("/mbs/v1/recordings")
class RecordingApi {

    @Autowired
    private lateinit var recordingBoundary: RecordingBoundaryService

    @Transactional(readOnly = true)
    @RequestMapping(method = arrayOf(RequestMethod.GET), path = arrayOf("/{uuid}"))
    fun getRecording(@PathVariable("uuid") uuid: UUID): RecordingRes =
            recordingBoundary.findByIdentifier(uuid)
                    .toRes()

    @Transactional(readOnly = true)
    @RequestMapping(method = arrayOf(RequestMethod.GET))
    fun getRecordings(@RequestParam("releaseGroupId") releaseGroupId: UUID,
                      @RequestParam("title") title: String,
                      pageable: Pageable) =
            recordingBoundary.findRecordingByReleaseGroupIdentifierAndName(releaseGroupId, title, pageable)
                    .toRes(Recording::toRes)
}

