package backend.src.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

import backend.src.entity.Anime;

@Repository
public interface AnimeRepository extends JpaRepository<Anime, Long> {
    @Query(value = "SELECT * FROM anime_table ORDER BY RANDOM() LIMIT 1", nativeQuery = true)
    Optional<Anime> findRandomAnime();
}
