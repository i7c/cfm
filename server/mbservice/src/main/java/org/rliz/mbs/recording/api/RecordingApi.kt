package org.rliz.mbs.recording.api

import org.rliz.mbs.recording.boundary.RecordingBoundaryService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestMethod
import org.springframework.web.bind.annotation.RestController
import java.util.*

@RestController
@RequestMapping("/mbs/v1/recordings")
class RecordingApi {

    @Autowired
    private lateinit var recordingBoundary: RecordingBoundaryService

    @RequestMapping(method = arrayOf(RequestMethod.GET), path = arrayOf("/{uuid}"))
    fun getRecording(@PathVariable("uuid") uuid: UUID): RecordingRes =
            recordingBoundary.findByIdentifier(uuid)
                    .toRes()
}

