package org.rliz.cfm.recorder.recording.boundary

import org.rliz.cfm.recorder.common.exception.InvalidResourceException
import org.rliz.cfm.recorder.recording.data.Recording
import org.rliz.cfm.recorder.recording.data.RecordingRepo
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Service
import java.util.*

@Service
class RecordingBoundary {

    @Autowired
    lateinit var recordingRepo: RecordingRepo

    @Value("\${cfm.mbs.url}")
    lateinit var mbsUrl: String

    private fun findRecording(uuid: UUID): Recording? = recordingRepo.findOneByUuid(uuid)

    fun saveOrUpdate(new: Recording): Recording =
            findRecording(new.uuid ?: throw InvalidResourceException(Recording::uuid))
                    .let { existing ->
                        when {
                            existing == null -> recordingRepo.save(new)
                            existing.lastUpdated!!.before(new.lastUpdated) -> {
                                existing.artists = new.artists
                                existing.title = new.title
                                existing.lastUpdated = new.lastUpdated
                                recordingRepo.save(existing)
                            }
                            else -> existing
                        }
                    }
}