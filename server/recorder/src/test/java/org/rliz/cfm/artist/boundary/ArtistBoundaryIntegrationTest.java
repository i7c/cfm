package org.rliz.cfm.artist.boundary;

import com.google.common.collect.Lists;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import org.rliz.cfm.artist.boundary.impl.ArtistBoundaryServiceImpl;
import org.rliz.cfm.artist.builder.ArtistBuilder;
import org.rliz.cfm.artist.model.Artist;
import org.rliz.cfm.artist.repository.ArtistRepository;
import org.rliz.cfm.musicbrainz.api.MusicbrainzRestClient;
import org.rliz.cfm.musicbrainz.api.dto.MbArtistDto;
import org.springframework.data.util.ReflectionUtils;

import java.util.List;
import java.util.UUID;

import static org.mockito.Matchers.any;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.when;
import static org.springframework.util.Assert.isTrue;

/**
 * Test {@link ArtistBoundaryService}.
 */
@RunWith(MockitoJUnitRunner.class)
public class ArtistBoundaryIntegrationTest {

    @Mock
    private ArtistRepository artistRepository;

    @Mock
    private MusicbrainzRestClient musicbrainzRestClient;

    @InjectMocks
    ArtistBoundaryService cut = new ArtistBoundaryServiceImpl(artistRepository, musicbrainzRestClient);

    /**
     * Verifies the semantics of getting from the repo or retrieving an artist from musicbrainz.
     *
     * @throws NoSuchFieldException not expected
     */
    @Test
    public void testGetOrCreateArtistsWithMusicbrainz() throws NoSuchFieldException {
        UUID artist1Mbid = UUID.randomUUID();
        UUID artist2Mbid = UUID.randomUUID();
        List<UUID> requestArtistsMbids = Lists.newArrayList(artist1Mbid, artist2Mbid);

        Artist artist1 = ArtistBuilder.artist()
                .withMbid(artist1Mbid)
                .withIdentifier(UUID.randomUUID())
                .withName("Artist One")
                .build();

        MbArtistDto mbArtistDto = new MbArtistDto();
        ReflectionUtils.setField(MbArtistDto.class.getDeclaredField("name"), mbArtistDto, "Artist 2");
        ReflectionUtils.setField(MbArtistDto.class.getDeclaredField("mbid"), mbArtistDto, artist2Mbid);

        when(artistRepository.save(any(Artist.class)))
                .thenAnswer(call -> {
                    Artist artistToSave = (Artist) call.getArguments()[0];
                    return artistToSave;
                });
        when(artistRepository.findByMbidIn(eq(requestArtistsMbids)))
                .thenReturn(Lists.newArrayList(artist1));
        when(musicbrainzRestClient.getArtist(eq(artist2Mbid), eq(MusicbrainzRestClient.FORMAT_JSON)))
                .thenReturn(mbArtistDto);

        // test starts here
        List<Artist> artists = cut.getOrCreateArtistsWithMusicbrainz(requestArtistsMbids);
        isTrue(artists.size() == 2);
        Artist foundArtist2 = artists.get(1);
        isTrue(artists.get(0).equals(artist1));
        isTrue("Artist 2".equals(foundArtist2.getName()));
        isTrue(artist2Mbid.equals(foundArtist2.getMbid()));
    }

}
