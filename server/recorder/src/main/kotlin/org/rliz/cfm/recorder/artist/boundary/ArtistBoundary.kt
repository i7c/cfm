package org.rliz.cfm.recorder.artist.boundary

import org.rliz.cfm.recorder.artist.data.Artist
import org.rliz.cfm.recorder.artist.data.ArtistRepo
import org.rliz.cfm.recorder.common.exception.InvalidResourceException
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service

@Service
class ArtistBoundary {

    @Autowired
    lateinit var artistRepo: ArtistRepo

    fun saveOrUpdate(artists: List<Artist>): List<Artist> {
        val storedArtists =
                artistRepo.findByUuidIn(
                        artists.map { it.uuid ?: throw InvalidResourceException(Artist::uuid) }.toList()
                )
                        .map { (it.uuid ?: throw InvalidResourceException(Artist::uuid)) to it }
                        .toMap()

        return artists.map { it to storedArtists[it.uuid] }.map {
            if (it.second == null) {
                it.first
            } else {
                val new: Artist = it.first
                val existing: Artist = it.second!!

                if (existing.lastUpdated!!.before(new.lastUpdated)) {
                    existing.name = new.name
                    existing.lastUpdated = new.lastUpdated
                    existing
                } else {
                    null
                }
            }
        }.filterNotNull().toList().let { artistRepo.save(it) }
    }
}