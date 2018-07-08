package org.rliz.cfm.recorder.stats.api

import org.rliz.cfm.recorder.common.api.toRes
import org.rliz.cfm.recorder.common.security.currentUser
import org.rliz.cfm.recorder.stats.boundary.StatsBoundary
import org.rliz.cfm.recorder.stats.data.FirstClassStatsType
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.data.domain.Pageable
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestMethod
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping(path = ["/rec/v1/stats"])
class StatsApi {

    @Autowired
    lateinit var statsBoundary: StatsBoundary

    @RequestMapping(method = [RequestMethod.GET], path = ["/{type}"])
    fun getFirstClassStats(
        @PathVariable type: String,
        @RequestParam(defaultValue = "false") mine: Boolean,
        pageable: Pageable
    ) =
        statsBoundary.getFirstClassStats(
            type = FirstClassStatsType.valueOf(type.toUpperCase()),
            user = if (mine) currentUser() else null,
            pageable = pageable
        ).toRes { it }
}
