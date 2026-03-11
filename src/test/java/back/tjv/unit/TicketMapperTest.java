package back.tjv.unit;

import back.tjv.data.dto.ticket.TicketCreateDto;
import back.tjv.data.dto.ticket.TicketUpdateDto;
import back.tjv.data.entity.Ticket;
import back.tjv.data.mapper.TicketMapper;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class TicketMapperTest {

    @Test
    void fromCreateDto_parsesSeatId() {
        TicketCreateDto dto = new TicketCreateDto(1L, 2L, "B7");
        Ticket t = TicketMapper.fromCreateDto(dto);
        assertEquals("B", t.getRowLabel());
        assertEquals(7, t.getSeatNumber());
    }

    @Test
    void fromCreateDto_rejectsInvalidSeatId() {
        TicketCreateDto dto = new TicketCreateDto(1L, 2L, "1A");
        assertThrows(IllegalArgumentException.class, () -> TicketMapper.fromCreateDto(dto));
    }

    @Test
    void applyUpdate_updatesSeatId() {
        Ticket t = new Ticket();
        t.setRowLabel("A");
        t.setSeatNumber(1);
        TicketUpdateDto dto = new TicketUpdateDto("C10", null);
        TicketMapper.applyUpdate(t, dto);
        assertEquals("C", t.getRowLabel());
        assertEquals(10, t.getSeatNumber());
    }
}
