package back.tjv.integration;

import back.tjv.data.entity.Buyer;
import back.tjv.data.entity.Event;
import back.tjv.data.entity.Speaker;
import back.tjv.data.entity.Ticket;
import back.tjv.data.repository.BuyerRepository;
import back.tjv.data.repository.EventRepository;
import back.tjv.data.repository.SpeakerRepository;
import back.tjv.data.repository.TicketRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.math.BigDecimal;
import java.time.OffsetDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
@Testcontainers
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class EventRepositoryIntegrationTest {

    @Container
    static PostgreSQLContainer<?> postgres = new PostgreSQLContainer<>("postgres:16");

    @DynamicPropertySource
    static void registerDb(DynamicPropertyRegistry registry) {
        registry.add("spring.datasource.url", postgres::getJdbcUrl);
        registry.add("spring.datasource.username", postgres::getUsername);
        registry.add("spring.datasource.password", postgres::getPassword);
        registry.add("spring.jpa.hibernate.ddl-auto", () -> "create-drop");
    }

    @Autowired EventRepository eventRepository;
    @Autowired BuyerRepository buyerRepository;
    @Autowired TicketRepository ticketRepository;
    @Autowired SpeakerRepository speakerRepository;

    @Test
    void findDistinctByBuyerId_returnsEvents() {
        Buyer buyer = buyerRepository.save(new Buyer("Alice", "alice+repo@example.com"));

        Event event = new Event("Repo Event",
                OffsetDateTime.parse("2035-01-12T10:00:00Z"),
                OffsetDateTime.parse("2035-01-12T12:00:00Z"));
        event.setDescription("Desc");
        event.setVenue("Main");
        event.setPrice(new BigDecimal("100.00"));
        event = eventRepository.save(event);

        Ticket ticket = new Ticket();
        ticket.setBuyer(buyer);
        ticket.setEvent(event);
        ticket.setPrice(event.getPrice());
        ticket.setRowLabel("A");
        ticket.setSeatNumber(1);
        ticketRepository.save(ticket);

        List<Event> out = eventRepository.findDistinctByBuyerId(buyer.getId());
        assertEquals(1, out.size());
        assertEquals(event.getId(), out.get(0).getId());
    }

    @Test
    void findBySpeakers_Id_returnsEvents() {
        Speaker speaker = speakerRepository.save(new Speaker("Speaker One"));

        Event event = new Event("Talk",
                OffsetDateTime.parse("2036-02-01T10:00:00Z"),
                OffsetDateTime.parse("2036-02-01T12:00:00Z"));
        event.setDescription("Desc");
        event.setVenue("Hall");
        event.setPrice(new BigDecimal("50.00"));
        event.addSpeaker(speaker);
        event = eventRepository.save(event);

        List<Event> out = eventRepository.findBySpeakers_Id(speaker.getId());
        assertEquals(1, out.size());
        assertEquals(event.getId(), out.get(0).getId());
    }
}
