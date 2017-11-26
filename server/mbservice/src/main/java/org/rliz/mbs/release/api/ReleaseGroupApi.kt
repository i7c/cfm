package org.rliz.mbs.release.api

import org.rliz.mbs.common.api.toHttpResponse
import org.rliz.mbs.common.api.toRes
import org.rliz.mbs.release.boundary.ReleaseGroupBoundaryService
import org.rliz.mbs.release.model.ReleaseGroup
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.data.domain.Pageable
import org.springframework.http.HttpStatus
import org.springframework.transaction.annotation.Transactional
import org.springframework.web.bind.annotation.*
import java.util.*

@RestController
@RequestMapping("/mbs/v1/releasegroups")
class ReleaseGroupApi {

    @Autowired
    private lateinit var releaseGroupBoundary: ReleaseGroupBoundaryService

    @Transactional(readOnly = true)
    @RequestMapping(method = arrayOf(RequestMethod.GET), value = "/{uuid}")
    fun getReleaseGroup(@PathVariable("uuid") uuid: UUID) =
            releaseGroupBoundary.findByIdentifier(uuid)
                    .toRes()
                    .toHttpResponse(HttpStatus.OK)

    @Transactional(readOnly = true)
    @RequestMapping(method = arrayOf(RequestMethod.GET), path = arrayOf("/identify"))
    fun getReleaseGroups(@RequestParam("artist") artists: List<String>,
                         @RequestParam("release") name: String,
                         pageable: Pageable) =
            releaseGroupBoundary.findByArtistsAndName(artists, name, pageable)
                    .toRes(ReleaseGroup::toRes)
                    .toHttpResponse(HttpStatus.OK)
}
