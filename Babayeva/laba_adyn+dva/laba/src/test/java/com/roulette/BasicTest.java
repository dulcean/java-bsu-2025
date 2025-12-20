package com.roulette;

import com.roulette.model.Ticket;
import com.roulette.repository.TicketRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.ActiveProfiles;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("test")
class TestBasic {

    @Autowired
    private TestRestTemplate restTemplate;

    @Autowired
    private TicketRepository ticketRepository;

    @BeforeEach
    void setUp() {
        ticketRepository.deleteAll();
    }

    @Test
    void testFullTicketFlow() {
        Ticket t = new Ticket();
        t.setQuestion("2+2?");
        t.setOptionA("4");
        t.setOptionB("5");
        t.setCorrectAnswer("4");
        t.setLearned(true);
        t.setTeacherName("belmondo");
        ticketRepository.save(t);

        // Запрос
        ResponseEntity<Ticket> response = restTemplate.getForEntity("/api/game/draw/belmondo", Ticket.class);
        Ticket body = response.getBody();

        // Проверки
        assertThat(response.getStatusCode().is2xxSuccessful()).isTrue();
        assertThat(body).isNotNull();
        assertThat(body.getOptionA()).isEqualTo("4");
        assertThat(body.isLearned()).isTrue();
    }
}