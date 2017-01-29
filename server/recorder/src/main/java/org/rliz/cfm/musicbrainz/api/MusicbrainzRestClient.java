package org.rliz.cfm.musicbrainz.api;

import org.rliz.cfm.musicbrainz.api.dto.MbArtistDto;
import org.rliz.cfm.musicbrainz.api.dto.MbRecordingDto;
import org.rliz.cfm.musicbrainz.api.dto.MbRecordingListDto;
import org.rliz.cfm.musicbrainz.api.dto.MbReleaseGroupDto;
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

    static final String FORMAT_JSON = "json";

    @RequestMapping(method = RequestMethod.GET, value = "/recording/{mbid}")
    MbRecordingDto getRecording(@PathVariable("mbid") UUID mbid, @RequestParam("inc") String includes,
                                @RequestParam("fmt") String responseFormat);

    @RequestMapping(method = RequestMethod.GET, value = "/artist/{mbid}")
    MbArtistDto getArtist(@PathVariable("mbid") UUID mbid, @RequestParam("fmt") String responseFormat);

    @RequestMapping(method = RequestMethod.GET, value = "/release-group/{mbid}")
    MbReleaseGroupDto getReleaseGroup(@PathVariable("mbid") UUID mbid,
                                      @RequestParam("inc") String includes,
                                      @RequestParam("fmt") String responseFormat);


    @RequestMapping(method = RequestMethod.GET, value = "/recording")
    MbRecordingListDto searchForRecordings(@RequestParam("query") String queryString,
                                           @RequestParam("fmt") String responseFormat);
}
