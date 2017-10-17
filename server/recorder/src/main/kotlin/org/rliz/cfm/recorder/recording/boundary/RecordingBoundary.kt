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

    fun saveOrUpdate(recording: Recording): Recording =
            findRecording(recording.uuid ?: throw InvalidResourceException(Recording::uuid))
                    .let {
                        when {
                            it == null -> recordingRepo.save(recording)
                            it.lastUpdated!!.before(recording.lastUpdated) -> {
                                it.artists = recording.artists
                                it.title = recording.title
                                it.lastUpdated = recording.lastUpdated
                                recordingRepo.save(it)
                            }
                            else -> it
                        }
                    }
}