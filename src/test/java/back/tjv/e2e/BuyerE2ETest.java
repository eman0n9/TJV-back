package back.tjv.e2e;

import back.tjv.TjvApplication;
import back.tjv.data.dto.buyer.BuyerCreateDto;
import back.tjv.data.dto.buyer.BuyerReadDto;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.boot.testcontainers.service.connection.ServiceConnection;
import org.springframework.http.*;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

@Testcontainers
@SpringBootTest(classes = TjvApplication.class,
        webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class BuyerE2ETest {

    @Container
    @ServiceConnection
    static PostgreSQLContainer<?> postgres = new PostgreSQLContainer<>("postgres:16");

    @LocalServerPort int port;

    @Autowired TestRestTemplate rest;

    private String baseUrl() { return "http://localhost:" + port + "/api/buyers"; }

    @Test
    void createThenGet_Buyer() {


        String unique = UUID.randomUUID().toString().substring(0, 8);
        BuyerCreateDto in = new BuyerCreateDto("E2E-" + unique, unique + "@ex.com");


        ResponseEntity<BuyerReadDto> created = rest.postForEntity(baseUrl(), in, BuyerReadDto.class);


        assertEquals(HttpStatus.CREATED, created.getStatusCode());
        assertNotNull(created.getBody());
        Long id = created.getBody().id();
        assertNotNull(id);


        ResponseEntity<BuyerReadDto> got = rest.getForEntity(baseUrl() + "/" + id, BuyerReadDto.class);


        assertEquals(HttpStatus.OK, got.getStatusCode());
        assertNotNull(got.getBody());
        assertEquals(id, got.getBody().id());
    }
}
