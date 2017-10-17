package org.rliz.cfm.recorder.artist.data

import org.springframework.data.jpa.repository.JpaRepository
import java.util.*

interface ArtistRepo : JpaRepository<Artist, Long> {

    fun findByUuidIn(uuids: List<UUID>): List<Artist>
}