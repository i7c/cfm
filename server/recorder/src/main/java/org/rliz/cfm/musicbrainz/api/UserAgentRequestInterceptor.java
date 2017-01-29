package org.rliz.cfm.musicbrainz.api;

import feign.RequestInterceptor;
import feign.RequestTemplate;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

/**
 * Can be used to set additional headers etc for requests with feign.
 */
@Component
public class UserAgentRequestInterceptor implements RequestInterceptor {

    @Value("${cfm.userAgent}")
    private String userAgent;

    @Override
    public void apply(RequestTemplate template) {
        template.header("User-Agent", userAgent);
        template.header("Accept", "application/json");
    }
}
