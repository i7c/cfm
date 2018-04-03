package org.rliz.mbs.release.api

import org.rliz.mbs.release.boundary.ReleaseGroupBoundary
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestMethod
import org.springframework.web.bind.annotation.RestController
import java.util.UUID

@RestController
@RequestMapping("/mbs/v2/release-groups")
class ReleaseGroupApi {

    @Autowired
    private lateinit var boundary: ReleaseGroupBoundary

    @RequestMapping(method = [RequestMethod.GET], path = ["/{id}"])
    fun getReleaseGroup(@PathVariable("id") id: UUID) =
        boundary.getReleaseGroup(id)
}
