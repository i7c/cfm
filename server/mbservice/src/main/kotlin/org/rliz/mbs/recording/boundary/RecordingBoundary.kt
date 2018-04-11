package org.rliz.mbs.recording.boundary

import org.rliz.mbs.common.error.NotFoundException
import org.rliz.mbs.recording.data.RecordingRepo
import org.rliz.mbs.recording.data.RecordingWithArtists
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import java.util.UUID

@Service
class RecordingBoundary {

    @Autowired
    private lateinit var recordingRepo: RecordingRepo

    fun getRecording(id: UUID): RecordingWithArtists =
        recordingRepo.getRecording(id) ?: throw NotFoundException("id=$id")

    fun getRecordings(ids: List<UUID>): List<RecordingWithArtists> =
        recordingRepo.getRecordings(ids)
}
