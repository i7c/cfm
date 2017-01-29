package org.rliz.cfm.artist.api;

import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Profile;

/**
 * Test configuration for {@link ArtistApiIntegrationTest}.
 */

@SpringBootApplication(scanBasePackages = "org.rliz.cfm.artist")
@Profile("test")
public class ArtistApiIntegrationConfig {
}
