package org.rliz.cfm.artist.api;

import com.google.common.collect.Lists;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.rliz.cfm.artist.api.dto.factory.ArtistDtoFactory;
import org.rliz.cfm.artist.api.dto.factory.ArtistListDtoFactory;
import org.rliz.cfm.artist.boundary.ArtistBoundaryService;
import org.rliz.cfm.artist.builder.ArtistBuilder;
import org.rliz.cfm.artist.model.Artist;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.web.config.EnableSpringDataWebSupport;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import java.util.UUID;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * Tests the {@link ArtistController} on REST level.
 */
@WebMvcTest(ArtistController.class)
@RunWith(SpringRunner.class)
@ContextConfiguration(classes = {ArtistDtoFactory.class, ArtistListDtoFactory.class, ArtistController.class})
@WithMockUser("somebody")
@EnableSpringDataWebSupport
public class ArtistApiTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private ArtistBoundaryService artistBoundaryService;

    /**
     * Verifies that GET on the artist resource gets a paged list of artists.
     *
     * @throws Exception not expected
     */
    @Test
    public void testFindAll() throws Exception {
        final String artist1Name = "Artist 1";
        final UUID artist1Mbid = UUID.randomUUID();
        final UUID artist1Identifier = UUID.randomUUID();
        Artist artist1 = ArtistBuilder.artist()
                .withName(artist1Name)
                .withMbid(artist1Mbid)
                .withIdentifier(artist1Identifier)
                .build();
        final String artist2Name = "Artist 2";
        final UUID artist2Mbid = UUID.randomUUID();
        final UUID artist2Identifier = UUID.randomUUID();
        Artist artist2 = ArtistBuilder.artist()
                .withName(artist2Name)
                .withMbid(artist2Mbid)
                .withIdentifier(artist2Identifier)
                .build();
        PageRequest pageRequest = new PageRequest(0, 20);
        when(artistBoundaryService.findAllArtists(pageRequest))
                .thenReturn(new PageImpl<Artist>(Lists.newArrayList(artist1, artist2), pageRequest, 2));

        mockMvc.perform(get(ArtistController.MAPPING_PATH)
                .accept(MediaType.APPLICATION_JSON_UTF8)
        )
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.elements[0].name")
                        .value(artist1Name))
                .andExpect(jsonPath("$.elements[1].name")
                        .value(artist2Name))
                .andExpect(jsonPath("$.elements[0].mbid")
                        .value(String.valueOf(artist1Mbid)))
                .andExpect(jsonPath("$.elements[1].mbid")
                        .value(String.valueOf(artist2Mbid)))
                .andExpect(jsonPath("$.elements[0].identifier")
                        .value(String.valueOf(artist1Identifier)))
                .andExpect(jsonPath("$.elements[1].identifier")
                        .value(String.valueOf(artist2Identifier)))
                .andExpect(jsonPath("$.pageNumber")
                        .value(0))
                .andExpect(jsonPath("$.totalPages")
                        .value(1))
                .andExpect(jsonPath("$.totalElements")
                        .value(2))
                .andExpect(jsonPath("$.pageSize")
                        .value(20));
    }

    /**
     * Verifies retrieving a single artist by identifier.
     *
     * @throws Exception not expected
     */
    @Test
    public void testFindOneByIdentifier() throws Exception {
        UUID artistIdentifier = UUID.randomUUID();
        UUID artistMbid = UUID.randomUUID();
        String artistName = "So And So";
        Artist artist = ArtistBuilder.artist()
                .withName(artistName)
                .withIdentifier(artistIdentifier)
                .withMbid(artistMbid)
                .build();
        when(artistBoundaryService.findOneByIdentifier(artistIdentifier))
                .thenReturn(artist);

        mockMvc.perform(get(ArtistController.MAPPING_PATH + "/" + String.valueOf(artistIdentifier))
                .accept(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.identifier").value(String.valueOf(artistIdentifier)))
                .andExpect(jsonPath("$.mbid").value(String.valueOf(artistMbid)))
                .andExpect(jsonPath("$.name").value(artistName));
    }

    /**
     * Verifies that unknown identifier leads to 404.
     *
     * @throws Exception not expected
     */
    @Test
    public void testFindWithWrongIdentifier() throws Exception {
        mockMvc.perform(get(ArtistController.MAPPING_PATH + "/" + String.valueOf(UUID.randomUUID()))
                .accept(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(status().isNotFound());
    }

    /**
     * Verifies that wrong request leads to
     *
     * @throws Exception not expected
     */
    @Test
    public void testBullshitRequest() throws Exception {
        mockMvc.perform(get(ArtistController.MAPPING_PATH + "/asdfbullcrap")
                .accept(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(status().isBadRequest());
    }
}
