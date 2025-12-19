package backend.src.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import backend.src.entity.Anime;
import backend.src.repository.AnimeRepository;
import javax.sql.DataSource;

@Service
public class AnimeService {
    @Autowired
    private AnimeRepository personRepository;

    @Autowired
    private DataSource dataSource;

    public Anime getRandomPerson() {
        return personRepository.findRandomAnime()
                .orElseThrow(() -> new RuntimeException("Сегодня без аниме :("));
    }
}