package org.rliz.cfm.recorder.playback.data

import org.springframework.data.jpa.repository.JpaRepository
import java.util.*

interface NowPlayingRepo : JpaRepository<NowPlaying, Long> {

    fun findOneByUserUuid(uuid: UUID): NowPlaying?

}
