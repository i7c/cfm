package org.rliz.mbs.recording.api

import org.rliz.mbs.recording.data.Recording

fun Recording.toRes() = RecordingRes(
        name = this.name,
        length = this.length,
        artists = this.artistCredit.artistCreditName.map { it.artist.name }
)
