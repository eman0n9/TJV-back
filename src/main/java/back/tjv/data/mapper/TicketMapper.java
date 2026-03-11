package back.tjv.data.mapper;

import back.tjv.data.dto.ticket.TicketCreateDto;
import back.tjv.data.dto.ticket.TicketReadDto;
import back.tjv.data.dto.ticket.TicketUpdateDto;
import back.tjv.data.entity.Ticket;

public final class TicketMapper {
    private TicketMapper() { }



    private static String toSeatId(String row, Integer seat) {
        if (row == null || seat == null) return null;
        return row + seat;
    }


    private static String[] parseSeatId(String seatId) {
        if (seatId == null) throw new IllegalArgumentException("seatId is required");
        String s = seatId.trim().toUpperCase();
        if (s.length() < 2) throw new IllegalArgumentException("Invalid seatId: " + seatId);

        String row = s.substring(0, 1);
        String numStr = s.substring(1);


        try {
            Integer.parseInt(numStr);
        } catch (NumberFormatException ex) {
            throw new IllegalArgumentException("Invalid seatId: " + seatId);
        }
        return new String[] { row, numStr };
    }



    public static TicketReadDto toReadDto(Ticket e) {
        if (e == null) return null;
        Long eventId = (e.getEvent() != null) ? e.getEvent().getId() : null;
        Long buyerId = (e.getBuyer() != null) ? e.getBuyer().getId() : null;

        return new TicketReadDto(
                e.getId(),
                eventId,
                buyerId,
                e.getPrice(),
                e.getPurchasedAt(),
                toSeatId(e.getRowLabel(), e.getSeatNumber())
        );
    }

    public static Ticket fromCreateDto(TicketCreateDto d) {
        if (d == null) return null;
        Ticket e = new Ticket();
        String[] p = parseSeatId(d.seatId());
        e.setRowLabel(p[0]);
        e.setSeatNumber(Integer.valueOf(p[1]));
        return e;
    }

    public static void applyUpdate(Ticket e, TicketUpdateDto d) {
        if (e == null || d == null) return;


        var seatId = d.seatId();
        if (seatId != null && !seatId.isBlank()) {
            String[] p = parseSeatId(seatId);
            e.setRowLabel(p[0]);
            e.setSeatNumber(Integer.valueOf(p[1]));
        }

        if (d.price() != null) {
            e.setPrice(d.price());
        }
    }
}