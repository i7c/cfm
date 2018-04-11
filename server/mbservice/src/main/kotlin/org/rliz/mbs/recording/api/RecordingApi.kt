package org.rliz.mbs.recording.api

import org.rliz.mbs.common.api.StupidListRes
import org.rliz.mbs.recording.boundary.RecordingBoundary
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestMethod
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController
import java.util.UUID

@RestController
@RequestMapping("/mbs/v2/recordings")
class RecordingApi {

    @Autowired
    private lateinit var recordingBoundary: RecordingBoundary

    @RequestMapping(method = [RequestMethod.GET], path = ["/{id}"])
    fun getRecording(@PathVariable("id") id: UUID) =
        recordingBoundary.getRecording(id)

    @RequestMapping(method = [RequestMethod.GET])
    fun getRecordings(@RequestParam("id") ids: List<UUID>) =
        recordingBoundary.getRecordings(ids)
            .let { StupidListRes(it) }
}
