package org.rliz.cfm.recorder.playback.data

import org.springframework.data.jpa.repository.JpaRepository

interface RawPlaybackDataRepo : JpaRepository<RawPlaybackData, Long>
