package back.tjv.config;

import back.tjv.data.entity.*;
import back.tjv.data.repository.*;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

import java.math.BigDecimal;
import java.time.OffsetDateTime;
import java.util.List;

@Configuration
@Profile("dev")
public class DevDataSeeder {

    @Bean
    public org.springframework.boot.CommandLineRunner seedData(
            BuyerRepository buyerRepo,
            SpeakerRepository speakerRepo,
            EventRepository eventRepo,
            TicketRepository ticketRepo
    ) {
        return args -> {
            if (buyerRepo.count() > 0 || speakerRepo.count() > 0 || eventRepo.count() > 0 || ticketRepo.count() > 0) {
                return;
            }


            Buyer alice = buyerRepo.save(new Buyer("Alice", "alice@example.com"));
            Buyer bob   = buyerRepo.save(new Buyer("Bob",   "bob@example.com"));


            Speaker sp1 = new Speaker("Liu Yang");
            sp1.setPhoto("/image/sp4.png");
            sp1.setRole("Indie Illustrator");
            Speaker sp2 = new Speaker("Max Pérez");
            sp2.setPhoto("/image/sp2.png");
            sp2.setRole("Concept Artist");
            sp1 = speakerRepo.save(sp1);
            sp2 = speakerRepo.save(sp2);


            Event e1 = new Event("Digital Storytelling",
                    OffsetDateTime.parse("2030-03-10T10:00:00Z"),
                    OffsetDateTime.parse("2030-03-10T12:00:00Z"));
            e1.setDescription("Demo description for Digital Storytelling.");
            e1.setVenue("Main Auditorium");
            e1.setPrice(new BigDecimal("100.00"));
            e1.addSpeaker(sp1);
            e1.addSpeaker(sp2);

            Event e2 = new Event("Illustration Masterclass",
                    OffsetDateTime.parse("2030-05-01T10:00:00Z"),
                    OffsetDateTime.parse("2030-05-01T12:00:00Z"));
            e2.setDescription("Demo description for Illustration Masterclass.");
            e2.setVenue("Main Auditorium");
            e2.setPrice(new BigDecimal("150.00"));
            e2.addSpeaker(sp1);

            e1 = eventRepo.save(e1);
            e2 = eventRepo.save(e2);


            Ticket t1 = new Ticket();
            t1.setEvent(e1);
            t1.setBuyer(alice);
            t1.setPrice(e1.getPrice());
            t1.setRowLabel("A");
            t1.setSeatNumber(1);

            Ticket t2 = new Ticket();
            t2.setEvent(e1);
            t2.setBuyer(bob);
            t2.setPrice(e1.getPrice());
            t2.setRowLabel("A");
            t2.setSeatNumber(2);

            Ticket t3 = new Ticket();
            t3.setEvent(e2);
            t3.setBuyer(alice);
            t3.setPrice(e2.getPrice());
            t3.setRowLabel("B");
            t3.setSeatNumber(5);

            ticketRepo.saveAll(List.of(t1, t2, t3));
        };
    }
}
