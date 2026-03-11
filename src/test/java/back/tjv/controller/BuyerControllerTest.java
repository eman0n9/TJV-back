
package back.tjv.controller;

import back.tjv.business.service.BuyerService;
import back.tjv.business.service.TicketService;
import back.tjv.data.dto.buyer.BuyerCreateDto;
import back.tjv.data.dto.buyer.BuyerReadDto;
import back.tjv.data.dto.buyer.BuyerUpdateDto;
import back.tjv.exception.ApiExceptionHandler;
import back.tjv.exception.ConflictException;
import back.tjv.exception.NotFoundException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.time.OffsetDateTime;
import java.util.List;

import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.is;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(BuyerController.class)
@Import(ApiExceptionHandler.class)
class BuyerControllerTest {

    @Autowired MockMvc mvc;
    @Autowired ObjectMapper om;

    @MockitoBean
    BuyerService buyerService;
    @MockitoBean
    TicketService ticketService;

    @Test
    void postCreate_Returns201AndLocation() throws Exception {
        BuyerCreateDto in = new BuyerCreateDto("Ana", "ana@example.com");
        BuyerReadDto out = new BuyerReadDto(1L, "Ana", "ana@example.com", OffsetDateTime.now());
        when(buyerService.create(any(BuyerCreateDto.class))).thenReturn(out);

        mvc.perform(post("/api/buyers")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(om.writeValueAsString(in)))
                .andExpect(status().isCreated())
                .andExpect(header().string("Location", containsString("/api/buyers/1")))
                .andExpect(jsonPath("$.id", is(1)))
                .andExpect(jsonPath("$.name", is("Ana")))
                .andExpect(jsonPath("$.email", is("ana@example.com")));
    }

    @Test
    void getNotFound_MappedTo404() throws Exception {
        when(buyerService.get(99L)).thenThrow(new NotFoundException("Buyer not found: id=99"));

        mvc.perform(get("/api/buyers/99"))
                .andExpect(status().isNotFound())
                .andExpect(content().string(containsString("not found")));
    }

    @Test
    void putUpdate_ConflictMappedTo409() throws Exception {
        when(buyerService.update(eq(1L), any(BuyerUpdateDto.class)))
                .thenThrow(new ConflictException("Buyer with email already exists: x@x"));

        BuyerUpdateDto in = new BuyerUpdateDto("Ana", "x@x");
        mvc.perform(put("/api/buyers/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(om.writeValueAsString(in)))
                .andExpect(status().isConflict())
                .andExpect(content().string(containsString("already exists")));
    }

    @Test
    void list_Returns200WithArray() throws Exception {
        BuyerReadDto a = new BuyerReadDto(1L, "A", "a@ex.com", OffsetDateTime.now());
        BuyerReadDto b = new BuyerReadDto(2L, "B", "b@ex.com", OffsetDateTime.now());
        when(buyerService.list()).thenReturn(List.of(a, b));

        mvc.perform(get("/api/buyers"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id", is(1)))
                .andExpect(jsonPath("$[1].id", is(2)));
    }
}
