package org.rliz.mbs.playback.boundary

import org.rliz.mbs.recording.boundary.RecordingBoundaryService
import org.rliz.mbs.release.boundary.ReleaseBoundaryService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service

@Service
class PlaybackBoundary {

    @Autowired
    private lateinit var releaseBoundaryService: ReleaseBoundaryService

    @Autowired
    private lateinit var recordingBoundaryService: RecordingBoundaryService


    fun identifyPlayback(artists: List<String>, title: String, release: String, rgthres: Int, recthres: Int) =
            releaseBoundaryService.identifyRelease(artists, release, rgthres).let {
                Pair(it, recordingBoundaryService.identifyRecording(it, title, recthres))
            }
}
