package org.rliz.cfm.musicbrainz.api;

import org.rliz.cfm.musicbrainz.api.dto.MusicbrainzRecording;
import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.UUID;

/**
 * REST Client to query Musicbrainz.
 */
@FeignClient(name = "musicbrainz", url = "http://musicbrainz.org/ws/2")
public interface MusicbrainzRestClient {

    @RequestMapping(method = RequestMethod.GET, value = "/recording/{mbid}")
    MusicbrainzRecording getRecording(@PathVariable("mbid") UUID mbid, @RequestParam("inc") String includes,
                                      @RequestParam("fmt") String responseFormat);
}
