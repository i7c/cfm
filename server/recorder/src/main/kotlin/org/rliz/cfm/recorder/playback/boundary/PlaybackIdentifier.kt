package org.rliz.cfm.recorder.playback.boundary

import org.rliz.cfm.recorder.artist.boundary.ArtistBoundary
import org.rliz.cfm.recorder.common.exception.MbsLookupFailedException
import org.rliz.cfm.recorder.common.rest.restCall
import org.rliz.cfm.recorder.mbs.api.MbsArtistDto
import org.rliz.cfm.recorder.mbs.api.MbsIdentifyRes
import org.rliz.cfm.recorder.mbs.trans.toDto
import org.rliz.cfm.recorder.mbs.trans.toEntity
import org.rliz.cfm.recorder.recording.boundary.RecordingBoundary
import org.rliz.cfm.recorder.recording.data.Recording
import org.rliz.cfm.recorder.relgroup.boundary.ReleaseGroupBoundary
import org.rliz.cfm.recorder.relgroup.data.ReleaseGroup
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Service
import org.springframework.web.util.UriComponentsBuilder

@Service
class PlaybackIdentifier {

    @Value("\${cfm.mbs.url}")
    lateinit var mbsUrl: String

    @Autowired
    lateinit var artistBoundary: ArtistBoundary

    @Autowired
    lateinit var recordingBoundary: RecordingBoundary

    @Autowired
    lateinit var releaseGroupBoundary: ReleaseGroupBoundary

    fun identify(recordingTitle: String, releaseTitle: String, artists: List<String>): Pair<Recording, ReleaseGroup> =
            requestIdentifyPlayback(recordingTitle, releaseTitle, artists)
                    .toDto()
                    .let {
                        Pair(
                                it.recording
                                        .toEntity(
                                                it.recording.artists
                                                        .map(MbsArtistDto::toEntity)
                                                        .let(artistBoundary::saveOrUpdate))
                                        .let(recordingBoundary::saveOrUpdate),

                                it.releaseGroup
                                        .toEntity(
                                                it.releaseGroup.artists
                                                        .map(MbsArtistDto::toEntity)
                                                        .let(artistBoundary::saveOrUpdate))
                                        .let(releaseGroupBoundary::saveOrUpdate)
                        )
                    }


    /**
     * Retrieve an IdentifyPlaybackRes from mbs service. The result can be null if the request was not successful.
     */
    private fun requestIdentifyPlayback(recordingTitle: String, releaseTitle: String, artists: List<String>):
            MbsIdentifyRes {

        val uriTemplate = UriComponentsBuilder.fromHttpUrl(mbsUrl)
                .pathSegment("mbs", "v1", "playbacks", "identify")
                .queryParam("full", "true")
                .queryParam("title", recordingTitle)
                .queryParam("release", releaseTitle)
        artists.forEach { uriTemplate.queryParam("artist", it) }

        return try {
            restCall().getForObject(uriTemplate.build().toUri(), MbsIdentifyRes::class.java)
        } catch (e: Exception) {
            throw MbsLookupFailedException(e)
        }
    }

}

