package org.rliz.mbs.recording.api

import org.rliz.mbs.recording.data.Recording

data class RecordingRes(
        val name: String? = null,
        val length: Long? = null,
        val artists: List<String> = emptyList()
)

fun Recording.toRes() = RecordingRes(
        name = this.name,
        length = this.length,
        artists = this.artistCredit.artistCreditName.map { it.artist.name }
)
