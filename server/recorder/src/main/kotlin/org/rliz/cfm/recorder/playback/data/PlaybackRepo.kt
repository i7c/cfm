package org.rliz.cfm.recorder.playback.data

import org.springframework.data.jpa.repository.JpaRepository

interface PlaybackRepo : JpaRepository<Playback, Long>