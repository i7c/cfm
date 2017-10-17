package org.rliz.cfm.recorder.mbs.api

data class MbsIdentifyRes(
        val recording: MbsRecordingRes? = null,
        val releaseGroup: MbsReleaseGroupRes? = null
)

data class MbsIdentifyDto(
        val recording: MbsRecordingDto,
        val releaseGroup: MbsReleaseGroupDto
)
