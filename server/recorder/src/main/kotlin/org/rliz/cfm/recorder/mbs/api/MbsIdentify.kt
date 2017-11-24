package org.rliz.cfm.recorder.mbs.api

import org.rliz.cfm.recorder.common.trans.nonNullField

data class MbsIdentifyRes(
        val recording: MbsRecordingRes? = null,
        val releaseGroup: MbsReleaseGroupRes? = null
)

data class MbsIdentifyDto(
        val recording: MbsRecordingDto,
        val releaseGroup: MbsReleaseGroupDto
)

fun MbsIdentifyRes.toDto(): MbsIdentifyDto =
        MbsIdentifyDto(
                nonNullField(this::recording).toDto(),
                nonNullField(this::releaseGroup).toDto()
        )
