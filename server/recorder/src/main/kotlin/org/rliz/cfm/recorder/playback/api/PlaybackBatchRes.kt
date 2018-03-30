package org.rliz.cfm.recorder.playback.api

import javax.validation.constraints.Size

data class PlaybackBatchRes(
    @field:Size(max = 50) val playbacks: List<PlaybackRes> = emptyList()
)
