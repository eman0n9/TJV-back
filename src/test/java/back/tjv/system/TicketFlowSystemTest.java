package back.tjv.system;

import back.tjv.TjvApplication;
import back.tjv.data.dto.buyer.BuyerCreateDto;
import back.tjv.data.dto.buyer.BuyerReadDto;
import back.tjv.data.dto.event.EventCreateDto;
import back.tjv.data.dto.event.EventReadDto;
import back.tjv.data.dto.ticket.TicketReadDto;
import back.tjv.data.dto.ticket.TicketSeatDtos;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.boot.testcontainers.service.connection.ServiceConnection;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.math.BigDecimal;
import java.time.OffsetDateTime;
import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

@Testcontainers
@SpringBootTest(classes = TjvApplication.class,
        webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class TicketFlowSystemTest {

    @Container
    @ServiceConnection
    static PostgreSQLContainer<?> postgres = new PostgreSQLContainer<>("postgres:16");

    @LocalServerPort int port;

    @Autowired TestRestTemplate rest;

    private String baseUrl() { return "http://localhost:" + port; }

    @Test
    void purchaseFlow_createsTicketVisibleInBuyerList() {
        String unique = UUID.randomUUID().toString().substring(0, 8);

        BuyerCreateDto buyerIn = new BuyerCreateDto("User-" + unique, "user-" + unique + "@ex.com");
        ResponseEntity<BuyerReadDto> buyerRes = rest.postForEntity(baseUrl() + "/api/buyers", buyerIn, BuyerReadDto.class);
        assertEquals(HttpStatus.CREATED, buyerRes.getStatusCode());
        BuyerReadDto buyer = buyerRes.getBody();
        assertNotNull(buyer);

        EventCreateDto eventIn = new EventCreateDto(
                "Event-" + unique,
                "Desc",
                "Hall",
                OffsetDateTime.parse("2035-01-12T10:00:00Z"),
                OffsetDateTime.parse("2035-01-12T12:00:00Z"),
                new BigDecimal("120.00"),
                List.of()
        );
        ResponseEntity<EventReadDto> eventRes = rest.postForEntity(baseUrl() + "/api/events", eventIn, EventReadDto.class);
        assertEquals(HttpStatus.CREATED, eventRes.getStatusCode());
        EventReadDto event = eventRes.getBody();
        assertNotNull(event);

        TicketSeatDtos.PurchaseReq purchaseReq = new TicketSeatDtos.PurchaseReq(buyer.id(), List.of("A1"));
        ResponseEntity<TicketReadDto[]> purchaseRes = rest.postForEntity(
                baseUrl() + "/api/events/" + event.id() + "/tickets",
                purchaseReq,
                TicketReadDto[].class
        );
        assertEquals(HttpStatus.CREATED, purchaseRes.getStatusCode());
        assertNotNull(purchaseRes.getBody());
        assertEquals(1, purchaseRes.getBody().length);

        ResponseEntity<TicketReadDto[]> listRes = rest.getForEntity(
                baseUrl() + "/api/buyers/" + buyer.id() + "/tickets",
                TicketReadDto[].class
        );
        assertEquals(HttpStatus.OK, listRes.getStatusCode());
        assertNotNull(listRes.getBody());
        assertTrue(listRes.getBody().length >= 1);
    }
}
