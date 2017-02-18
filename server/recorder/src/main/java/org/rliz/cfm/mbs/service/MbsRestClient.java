package org.rliz.cfm.mbs.service;

import org.rliz.cfm.mbs.dto.MbArtistDto;
import org.rliz.cfm.mbs.dto.MbPlaybackDetailsDto;
import org.rliz.cfm.mbs.dto.MbRecordingDto;
import org.rliz.cfm.mbs.dto.MbReleaseGroupDto;
import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;
import java.util.UUID;

/**
 * REST client to query the Musicbrainz Service.
 */
@FeignClient(name = "mbs", url = "http://127.0.0.1:1338/api/v1")
public interface MbsRestClient {

    @RequestMapping("/artists/{identifier}")
    MbArtistDto getArtistByIdentifier(@PathVariable("identifier") UUID identifier);

    @RequestMapping("/recordings/{identifier}")
    MbRecordingDto getRecordingByIdentifier(@PathVariable("identifier") UUID identifier);

    @RequestMapping("/releasegroups/{identifier}")
    MbReleaseGroupDto getReleaseGroupByIdentifier(@PathVariable("identifier") UUID identifier);

    @RequestMapping("/playbacks/identify")
    MbPlaybackDetailsDto identifyPlaybackWithNames(@RequestParam("artist")List<String> artists,
                                                   @RequestParam("title") String title,
                                                   @RequestParam("release") String release);

}
