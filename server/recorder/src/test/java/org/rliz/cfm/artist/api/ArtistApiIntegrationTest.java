package org.rliz.cfm.artist.api;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.rliz.cfm.artist.builder.ArtistBuilder;
import org.rliz.cfm.artist.model.Artist;
import org.rliz.cfm.artist.repository.ArtistRepository;
import org.rliz.cfm.musicbrainz.api.MusicbrainzRestClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Collections;
import java.util.UUID;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * Tests the {@link ArtistController} on REST level.
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = ArtistApiIntegrationConfig.class)
@AutoConfigureMockMvc
@ActiveProfiles("test")
@WithMockUser("somebody")
public class ArtistApiIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private ArtistRepository artistRepository;

    @MockBean
    MusicbrainzRestClient musicbrainzRestClient;


    /**
     * Verifies that GET on the artist resource gets a paged list of artists.
     *
     * @throws Exception not expected
     */
    @Test
    public void testFindAll() throws Exception {
        final String artistName = "AC/DC";
        final UUID artistMbid = UUID.randomUUID();
        final UUID artistIdentifier = UUID.randomUUID();
        Artist artist = ArtistBuilder.artist()
                .withName(artistName)
                .withMbid(artistMbid)
                .withIdentifier(artistIdentifier)
                .build();
        when(artistRepository.findAll(new PageRequest(0, 20)))
                .thenReturn(new PageImpl<Artist>(Collections.singletonList(artist)));

        mockMvc.perform(get(ArtistController.MAPPING_PATH)
                .accept(MediaType.APPLICATION_JSON)
        )
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.elements[0].name")
                        .value(artistName))
                .andExpect(jsonPath("$.elements[0].mbid")
                        .value(String.valueOf(artistMbid)))
                .andExpect(jsonPath("$.elements[0].identifier")
                        .value(String.valueOf(artistIdentifier)));
    }
}
