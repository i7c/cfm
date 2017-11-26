package org.rliz.mbs.recording.api

import org.rliz.mbs.recording.data.Recording
import java.util.*

data class RecordingRes(
        val id: UUID,
        val name: String,
        val length: Long? = null,
        val artists: List<String> = emptyList()
)

fun Recording.toRes() = RecordingRes(
        id = this.identifier,
        name = this.name,
        length = this.length,
        artists = this.artistCredit.artistCreditName.map { it.artist.name }
)
